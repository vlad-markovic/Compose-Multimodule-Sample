/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.vladmarkovic.sample.shared_domain.log.Lumber
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction
import com.vladmarkovic.sample.shared_presentation.composer.ComposeArgs
import com.vladmarkovic.sample.shared_presentation.composer.closeDrawer
import com.vladmarkovic.sample.shared_presentation.composer.onBack
import com.vladmarkovic.sample.shared_presentation.composer.openDrawer
import com.vladmarkovic.sample.shared_presentation.display.CommonDisplayAction
import com.vladmarkovic.sample.shared_presentation.navigation.CommonNavigationAction
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.navigation.ToScreen
import com.vladmarkovic.sample.shared_presentation.navigation.route
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.screen.ToScreenGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch


internal fun ComposeArgs.handleAction(action: BriefAction, bubbleUp: (BriefAction) -> Unit) {
    Lumber.i("action: ${action.javaClass.simpleName}")
    return when (action) {
        is NavigationAction -> navigate(action, bubbleUp)
        is CommonDisplayAction -> handleCommonDisplayAction(action)
        else -> bubbleUp(action)
    }
}

/** Branch out handling of different types of [NavigationAction]s. */
private fun ComposeArgs.navigate(action: NavigationAction, bubbleUp: (BriefAction) -> Unit) = when(action) {
    is ToScreen -> navController.navigate(action.route)
    is ToScreenGroup -> navController.context.handleTopScreenNavigationAction(action)
    is CommonNavigationAction -> navigate(action)
    else -> bubbleUp(action)
}

private fun ComposeArgs.navigate(action: CommonNavigationAction) {
    when (action) {
        is CommonNavigationAction.Back -> onBack()
    }
}

fun NavController.onBack(scaffoldState: ScaffoldState, scope: CoroutineScope) {
    when {
        scaffoldState.drawerState.isOpen -> scope.launch { scaffoldState.drawerState.close() }
        isStackFirstScreen -> context.asActivity.finish() // TODO circular if onBackPress is called; how to do for Fragments?
        else -> popBackStack()
    }
}

fun NavController.isInitialStackFirstScreen(initialScreen: Screen): Boolean =
    currentBackStackEntry?.isInitialStackFirstScreen(initialScreen) == true

val NavController.isStackFirstScreen: Boolean
    get() = currentBackStackEntry?.isStackFirstScreen == true

fun NavBackStackEntry.isInitialStackFirstScreen(initialScreen: Screen): Boolean =
    isStackFirstScreen && destination.parent?.startDestinationRoute == initialScreen.name

val NavBackStackEntry.isStackFirstScreen: Boolean
    get() = destination.parent?.startDestinationRoute == destination.route

private val NavDestination.topParent: NavGraph? get() = parent?.topParent()?.takeIf { it != this }

private fun NavGraph.topParent(): NavGraph {
    parentCounter++
    Lumber.e("Parent$parentCounter: route: $$route, parent: ${parent?.route}, start: $startDestinationRoute")
    return parent?.topParent() ?: this
}

private var parentCounter = 0


/** Enables separate back stack navigation per tab. */
fun NavController.navigate(tab: Tab<*>) {
    navigate(tab.name) {
        // Separate stacks per tab.
        popUpTo(graph.findStartDestination().id) {
            // Save state to be able to restore when re-selecting..
            saveState = true
        }
        // Avoid multiple instances when re-selecting the same tab.
        launchSingleTop = true
        // Restore state when re-selecting a previously selected tab.
        restoreState = true
    }
}

private fun ComposeArgs.handleCommonDisplayAction(action: CommonDisplayAction) =
    when(action) {
        is CommonDisplayAction.Toast -> {
            Toast.makeText(navController.context, action.value.get(navController.context), Toast.LENGTH_SHORT).show()
        }
        is CommonDisplayAction.OpenDrawer -> {
            openDrawer()
        }
        is CommonDisplayAction.CloseDrawer -> {
            closeDrawer()
        }
    }

@Composable
fun SetupTabsNavigation(tabs: Flow<Tab<*>>, navController: NavController) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        tabs.drop(1).collectWith(context.asActivity<ComponentActivity>()) {
            navController.navigate(it)
        }
    }
}
