/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.screen

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction

data class ScreenRouteData(
    val routeWithPlaceholders: String,
    val namedArgs: List<NamedNavArgument> = defaultArgs,
    val deepLinks: List<NavDeepLink> = defaultDeepLinks,
) : NavigationAction {
    private companion object {
        private val defaultArgs: List<NamedNavArgument> = emptyList()
        private val defaultDeepLinks: List<NavDeepLink> = emptyList()
    }
}
