/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.vladmarkovic.sample.shared_domain.screen.MainScreen
import com.vladmarkovic.sample.shared_domain.screen.Screen
import com.vladmarkovic.sample.shared_presentation.compose.opposite


val Screen.argNames: List<String>? get() = when(this) {
    MainScreen.PostsScreen.POST_SCREEN -> listOf(ScreenArgKeys.POST.name)
    MainScreen.CovidScreen.COVID_COUNTRY_INFO -> listOf(ScreenArgKeys.COUNTRY_INFO.name)
    else -> null
}

/** i.e. POSTS?POST={post-json} */
val Screen.route: String get() = name + routeArgs

/** i.e. ?POST=%s */
val Screen.routeArgsFormat: String
    get() = argNames
        ?.joinToString("&") { "${it}=%s" }
        ?.let { "?$it" } ?: ""

/** i.e. ?POST={POST} */
val Screen.routeArgs: String
    get() = argNames?.let { arguments ->
        String.format(routeArgsFormat, *arguments.map { "{${it}}" }.toTypedArray())
    } ?: ""

/** i.e. ?POST=[post-json] */
fun Screen.routeArgs(args: List<String>?): String {
    if (args.isNullOrEmpty()) return ""
    if (args.size != (this.argNames?.size ?: 0)) throw IllegalArgumentException()
    return String.format(routeArgsFormat, *args.toTypedArray())
}

/** Builds a [NamedNavArgument] of type String for a given [argName]. */
fun stringArg(argName: String) =
    navArgument(argName) {
        type = NavType.StringType
        nullable = false
        defaultValue = ""
    }

val Screen.namedArgs: List<NamedNavArgument>
    get() = argNames?.map { stringArg(it) } ?: emptyList()

val Screen.deepLinks: List<NavDeepLink>
    get() = emptyList()


private val screenTransitionAnimationSpec = tween<IntOffset>(200, easing = LinearEasing)

val Screen.enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)
    get() = {
        slideIntoContainer(
            animationSpec = screenTransitionAnimationSpec,
            towards = enterTransitionDirection()
        )
    }

val Screen.exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)
    get() = {
        slideOutOfContainer(
            animationSpec = screenTransitionAnimationSpec,
            towards = exitTransitionDirection()
        )
    }

fun Screen.enterTransitionDirection(): AnimatedContentTransitionScope.SlideDirection = when(this) {
    MainScreen.PostsScreen.FEED_SCREEN -> AnimatedContentTransitionScope.SlideDirection.End
    MainScreen.CovidScreen.COVID_COUNTRY_COMPARISON -> AnimatedContentTransitionScope.SlideDirection.Start
    else -> AnimatedContentTransitionScope.SlideDirection.Up
}

fun Screen.exitTransitionDirection(): AnimatedContentTransitionScope.SlideDirection =
    enterTransitionDirection().opposite
