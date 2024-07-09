/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import com.vladmarkovic.sample.shared_domain.screen.MainScreen
import com.vladmarkovic.sample.shared_domain.screen.NavGraphScreen
import com.vladmarkovic.sample.shared_domain.screen.SettingsScreen
import com.vladmarkovic.sample.shared_presentation.compose.animation.DefaultScreenTransition
import com.vladmarkovic.sample.shared_presentation.compose.animation.opposite
import com.vladmarkovic.sample.shared_presentation.compose.animation.slideEnterTransition
import com.vladmarkovic.sample.shared_presentation.compose.animation.slideExitTransition
import com.vladmarkovic.sample.shared_presentation.navigation.ToNavGraphScreen

val ToNavGraphScreen.navRoute: String get() = screen.name + screen.routeArgs(args)

val NavGraphScreen.routeData get() = ScreenRouteData(route, namedArgs, deepLinks, transitions())

/** i.e. POSTS?SCREEN=POST_SCREEN&ORDINAL=1&POST=[post-json] */
private val NavGraphScreen.route: String get() = name + routeArgs


/** i.e. ?SCREEN={SCREEN}&ORDINAL={ORDINAL} */
private val NavGraphScreen.routeArgs: String
    get() = allArgs.keys.toList().let { argNames ->
        "?" + String.format(argsFormat(argNames), *argNames.map { "{${it}}" }.toTypedArray())
    }

/** i.e. ?POST=[post-json] */
private fun NavGraphScreen.routeArgs(args: List<String>?): String =
    if (args == null) {
        joinArgs(defaultArgs)
    } else {
        if (args.size != extraArgs.size) throw IllegalArgumentException(
            "$args size ${args.size } should match $extraArgs size ${extraArgs.size}"
        )
        joinArgs(defaultArgs) + "&" + String.format(argsFormat(extraArgs.keys.toList()), *args.toTypedArray())
    }.let { "?$it" }

private val NavGraphScreen.namedArgs: List<NamedNavArgument> get() = allArgs.map { stringArg(it.key, it.value) }


/** Map of arg names and default values. For default args, the actual value is the same as the default value. */
private val NavGraphScreen.defaultArgs: Map<String, String> get() = mapOf(
    DefaultScreenArgNames.SCREEN.name to name,
    DefaultScreenArgNames.ORDINAL.name to ordinal.toString(),
    DefaultScreenArgNames.TRANSITION_DIRECTION.name to transitionDirection.toString()
)

private val NavGraphScreen.transitionDirection: AnimatedContentTransitionScope.SlideDirection
    get() = DefaultScreenTransition.enterDirection.let {
        when (this) {
            MainScreen.PostsScreen.FEED_SCREEN, SettingsScreen.MAIN -> it.opposite
            else -> it
        }
    }

/** Map of arg names and default values. For extra args, the default value is empty, and actual is passed in. */
private val NavGraphScreen.extraArgs: Map<String, String> get() = when(this) {
    MainScreen.PostsScreen.POST_SCREEN -> mapOf(ScreenArgNames.POST.name to "")
    MainScreen.CovidScreen.COVID_COUNTRY_INFO -> mapOf(ScreenArgNames.COUNTRY_INFO.name to "")
    else -> emptyMap()
}

/** Map of arg names and default values. */
private val NavGraphScreen.allArgs: Map<String, String> get() = defaultArgs + extraArgs
// endregion nav args

private val NavGraphScreen.deepLinks: List<NavDeepLink> get() = emptyList()

// region transitions
val NavGraphScreen.enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)
    get() = {
        slideIntoContainer(
            animationSpec = DefaultScreenTransition.animationSpec,
            towards = enterTransitionDirection(
                exitScreenOrdinal = targetState.arguments.argOrdinal,
                enterScreenOrdinal = initialState.arguments.argOrdinal,
            )
        )
    }

val NavGraphScreen.exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)
    get() = {
        slideOutOfContainer(
            animationSpec = DefaultScreenTransition.animationSpec,
            towards = exitTransitionDirection(
                exitScreenOrdinal = targetState.arguments.argOrdinal,
                enterScreenOrdinal = initialState.arguments.argOrdinal,
            )
        )
    }
private fun NavGraphScreen.enterTransitionDirection(
    exitScreenOrdinal: Int, enterScreenOrdinal: Int
): AnimatedContentTransitionScope.SlideDirection =
    when(this) {
        MainScreen.PostsScreen.FEED_SCREEN -> AnimatedContentTransitionScope.SlideDirection.End
        MainScreen.CovidScreen.COVID_COUNTRY_COMPARISON -> AnimatedContentTransitionScope.SlideDirection.Start
        else -> when {
            exitScreenOrdinal > enterScreenOrdinal -> AnimatedContentTransitionScope.SlideDirection.Start
            else -> AnimatedContentTransitionScope.SlideDirection.End
        }
    }

private fun NavGraphScreen.exitTransitionDirection(
    exitScreenOrdinal: Int, enterScreenOrdinal: Int
): AnimatedContentTransitionScope.SlideDirection =
    enterTransitionDirection(exitScreenOrdinal, enterScreenOrdinal).let {
        when(this) {
            MainScreen.PostsScreen.FEED_SCREEN, MainScreen.CovidScreen.COVID_COUNTRY_COMPARISON -> it.opposite
            else -> it
        }
    }

@Suppress("UnusedReceiverParameter")
private fun NavGraphScreen.transitions(): ScreenTransitions = ScreenTransitions(
    enter = { slideEnterTransition().invoke(this) },
    exit = { slideExitTransition().invoke(this) }
)
// endregion transitions