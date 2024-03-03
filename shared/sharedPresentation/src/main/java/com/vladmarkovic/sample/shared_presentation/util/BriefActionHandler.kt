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
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction
import com.vladmarkovic.sample.shared_presentation.composer.ComposeArgs
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderType
import com.vladmarkovic.sample.shared_presentation.composer.onBack
import com.vladmarkovic.sample.shared_presentation.composer.openDrawer
import com.vladmarkovic.sample.shared_presentation.display.CommonDisplayAction
import com.vladmarkovic.sample.shared_presentation.navigation.CommonNavigationAction
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.navigation.ToScreen
import com.vladmarkovic.sample.shared_presentation.navigation.route
import com.vladmarkovic.sample.shared_presentation.screen.ToScreenGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch


fun ComposeArgs.handleAction(
    type: ScreenHolderType,
    action: BriefAction,
    bubbleUp: (BriefAction) -> Unit
) = when (action) {
    is NavigationAction -> navigate(type, action, bubbleUp)
    is CommonDisplayAction -> handleCommonDisplayAction(action)
    else -> bubbleUp(action)
}

/** Branch out handling of different types of [NavigationAction]s. */
private fun ComposeArgs.navigate(
    type: ScreenHolderType,
    action: NavigationAction,
    bubbleUp: (BriefAction) -> Unit
) = when(action) {
    is ToScreen -> navController.navigate(action.route)
    is ToScreenGroup -> navController.context.handleTopScreenNavigationAction(action)
    is CommonNavigationAction -> navigate(type, action)
    else -> bubbleUp(action)
}

private fun ComposeArgs.navigate(type: ScreenHolderType, action: CommonNavigationAction) {
    when (action) {
        is CommonNavigationAction.Back -> onBack(type)
    }
}

fun NavController.onBack(type: ScreenHolderType, scaffoldState: ScaffoldState, scope: CoroutineScope) {
    when {
        scaffoldState.drawerState.isOpen -> scope.launch { scaffoldState.drawerState.close() }
        isInitialStackFirstScreen(type) -> context.asActivity.finish()
        else -> popBackStack()
    }
}

fun NavController.isInitialStackFirstScreen(type: ScreenHolderType): Boolean =
    currentBackStackEntry?.isStackFirstScreen == true && backQueue.size <= type.initialBackstackSize

val NavBackStackEntry.isStackFirstScreen: Boolean
    get() = destination.parent?.startDestinationRoute == destination.route



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
