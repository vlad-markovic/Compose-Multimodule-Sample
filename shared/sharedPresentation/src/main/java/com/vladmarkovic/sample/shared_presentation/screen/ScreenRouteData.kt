/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import com.vladmarkovic.sample.shared_domain.screen.NavGraphScreen
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction

data class ScreenRouteData(
    val route: String,
    val namedArgs: List<NamedNavArgument> = defaultArgs,
    val deepLinks: List<NavDeepLink> = defaultDeepLinks,
    val transitions: ScreenTransitions = defaultTransitions,
) : NavigationAction {
    companion object {
        val defaultArgs: List<NamedNavArgument> = emptyList()
        val defaultDeepLinks: List<NavDeepLink> = emptyList()
        val defaultTransitions: ScreenTransitions = ScreenTransitions.None
    }
}

data class ScreenTransitions(
    val enter: (AnimatedContentTransitionScope<NavBackStackEntry>.(NavGraphScreen) -> EnterTransition?)? = null,
    val exit: (AnimatedContentTransitionScope<NavBackStackEntry>.(NavGraphScreen) -> ExitTransition?)? = null,
    val popEnter: (AnimatedContentTransitionScope<NavBackStackEntry>.(NavGraphScreen) -> EnterTransition?)? = enter,
    val popExit: (AnimatedContentTransitionScope<NavBackStackEntry>.(NavGraphScreen) -> ExitTransition?)? = exit,
) {
    companion object {
        val None = ScreenTransitions()
    }
}
