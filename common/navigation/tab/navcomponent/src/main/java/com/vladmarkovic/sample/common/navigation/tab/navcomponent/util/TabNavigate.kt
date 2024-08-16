/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.navigation.tab.navcomponent.util

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.vladmarkovic.sample.common.navigation.tab.model.Tab


/** Enables separate back stack navigation per tab. */
fun NavController.navigate(tab: Tab<*>) {
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
