/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import android.widget.Toast
import androidx.compose.material.ScaffoldState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction
import com.vladmarkovic.sample.shared_presentation.composer.ContentArgs
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderType
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
import kotlinx.coroutines.launch


fun ContentArgs.handleAction(type: ScreenHolderType, action: BriefAction) =
    when (action) {
        is NavigationAction -> navigate(type, action)
        is CommonDisplayAction -> handleCommonDisplayAction(action)
        else -> throw IllegalArgumentException("Unhandled navigation action: $action")
    }

/** Branch out handling of different types of [NavigationAction]s. */
private fun ContentArgs.navigate(type: ScreenHolderType, action: NavigationAction) =
    when(action) {
        is Tab<*> -> navController.context.asActivity.setCurrentTab(action)
        is ToScreen -> navController.navigate(action.route)
        is ToScreenGroup -> navController.context.handleTopScreenNavigationAction(action)
        is CommonNavigationAction -> navigate(type, action)
        else -> throw IllegalArgumentException("Unhandled navigation action: $action")
    }

private fun ContentArgs.navigate(type: ScreenHolderType, action: CommonNavigationAction) {
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
fun <S: Screen, T: Tab<S>> NavController.navigate(tab: T) {
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

private fun ContentArgs.handleCommonDisplayAction(action: CommonDisplayAction) =
    when(action) {
        is CommonDisplayAction.Toast -> {
            Toast.makeText(navController.context, action.value.get(navController.context), Toast.LENGTH_SHORT).show()
        }
        is CommonDisplayAction.OpenDrawer -> {
            openDrawer()
        }
    }
