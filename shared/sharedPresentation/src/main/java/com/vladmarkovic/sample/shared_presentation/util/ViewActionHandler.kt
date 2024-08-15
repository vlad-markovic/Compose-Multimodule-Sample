/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.vladmarkovic.sample.core.android.asActivity
import com.vladmarkovic.sample.shared_domain.screen.NavGraphScreen
import com.vladmarkovic.sample.shared_domain.tab.Tab
import com.vladmarkovic.sample.shared_presentation.tab.route


fun NavController.onBack(isDrawerOpen: () -> Boolean, closeDrawer: () -> Unit) {
    when {
        isDrawerOpen() -> closeDrawer()
        isStackFirstScreen -> context.asActivity.finish() // TODO circular if onBackPress is called; how to do for Fragments?
        else -> popBackStack()
    }
}

fun NavController.isInitialStackFirstScreen(initialScreen: NavGraphScreen): Boolean =
    currentBackStackEntry?.isInitialStackFirstScreen(initialScreen) == true

val NavController.isStackFirstScreen: Boolean
    get() = currentBackStackEntry?.isStackFirstScreen == true

fun NavBackStackEntry.isInitialStackFirstScreen(initialScreen: NavGraphScreen): Boolean =
    isStackFirstScreen && destination.parent?.startDestinationRoute == initialScreen.name

val NavBackStackEntry.isStackFirstScreen: Boolean
    get() = destination.parent?.startDestinationRoute == destination.route

private val NavDestination.topParent: NavGraph? get() = parent?.topParent()?.takeIf { it != this }

private fun NavGraph.topParent(): NavGraph = parent?.topParent() ?: this

/** Enables separate back stack navigation per tab. */
fun NavController.navigate(tab: Tab) {
    navigate(tab.route) {
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
