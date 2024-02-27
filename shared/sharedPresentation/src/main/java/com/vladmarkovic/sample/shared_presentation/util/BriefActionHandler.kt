/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import android.widget.Toast
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction
import com.vladmarkovic.sample.shared_presentation.composer.ContentArgs
import com.vladmarkovic.sample.shared_presentation.display.CommonDisplayAction
import com.vladmarkovic.sample.shared_presentation.navigation.CommonNavigationAction
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.navigation.ToScreen
import com.vladmarkovic.sample.shared_presentation.navigation.route
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.screen.ToScreenGroup

fun ContentArgs.handleBriefAction(action: BriefAction) {
    return when (action) {
        is NavigationAction -> navigate(action)
        is CommonDisplayAction -> handleCommonDisplayAction(action)
        else -> throw IllegalArgumentException("Unhandled action: $action")
    }
}

/** Branch out handling of different types of [NavigationAction]s. */
private fun ContentArgs.navigate(action: NavigationAction) =
    when(action) {
        is Tab<*> -> context.asActivity.setCurrentTab(action)
        is ToScreen -> navController.navigate(action.route)
        is ToScreenGroup -> navController.context.handleTopScreenNavigationAction(action)
        is CommonNavigationAction -> navigate(action)
        else -> throw IllegalArgumentException("Unhandled navigation action: $action")
    }

private fun ContentArgs.navigate(action: CommonNavigationAction) =
    when (action) {
        is CommonNavigationAction.Back -> onBack()
    }

/** Enables separate back stack navigation per tab. */
fun <S: Screen, T: Tab<S>> NavHostController.navigate(tab: T) {
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
            Toast.makeText(context, action.value.get(context), Toast.LENGTH_SHORT).show()
        }
        is CommonDisplayAction.OpenDrawer -> {
            openDrawer()
        }
    }
