/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.navigation.screen.navcomponent.model

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink

data class ScreenRouteData(
    val routeWithPlaceholders: String,
    val namedArgs: List<NamedNavArgument> = defaultArgs,
    val deepLinks: List<NavDeepLink> = defaultDeepLinks,
) {
    private companion object {
        private val defaultArgs: List<NamedNavArgument> = emptyList()
        private val defaultDeepLinks: List<NavDeepLink> = emptyList()
    }
}
