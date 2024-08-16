/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.navigation.screen.navcomponent.util

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController


fun NavController.isScreenVisible(route: String?): Boolean =
    route != null && currentDestination?.route?.contains(route) == true

val NavController.isFirstScreen: Boolean
    get() = currentBackStackEntry?.isFirstScreen == true

val NavBackStackEntry.isFirstScreen: Boolean
    get() = destination.parent?.startDestinationRoute == destination.route
