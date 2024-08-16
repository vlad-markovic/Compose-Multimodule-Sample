/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import com.vladmarkovic.sample.common.navigation.screen.model.NavGraphScreen
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.util.isFirstScreen


fun NavController.isInitialStackFirstScreen(initialScreen: NavGraphScreen): Boolean =
    currentBackStackEntry?.isInitialStackFirstScreen(initialScreen) == true

fun NavBackStackEntry.isInitialStackFirstScreen(initialScreen: NavGraphScreen): Boolean =
    isFirstScreen && destination.parent?.startDestinationRoute?.startsWith(initialScreen.name) == true


private val NavDestination.topParent: NavGraph? get() = parent?.topParent()?.takeIf { it != this }

private fun NavGraph.topParent(): NavGraph = parent?.topParent() ?: this
