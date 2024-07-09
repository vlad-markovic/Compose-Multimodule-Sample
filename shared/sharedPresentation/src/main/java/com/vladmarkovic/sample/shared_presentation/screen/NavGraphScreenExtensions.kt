/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.screen

import android.os.Bundle
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
import com.vladmarkovic.sample.shared_domain.screen.NavGraphScreen
import com.vladmarkovic.sample.shared_presentation.compose.animation.opposite


val NavGraphScreen.argNames: List<String>? get() =
    if (ordinal == 0) null
    else listOf(ScreenArgKeys.ORDINAL.name) + (screenArgNames ?: emptyList())

val NavGraphScreen.screenArgNames: List<String>? get() = when(this) {
    MainScreen.PostsScreen.POST_SCREEN -> listOf(ScreenArgKeys.POST.name)
    MainScreen.CovidScreen.COVID_COUNTRY_INFO -> listOf(ScreenArgKeys.COUNTRY_INFO.name)
    else -> null
}

/** i.e. POSTS?POST={post-json} */
val NavGraphScreen.route: String get() = name + routeArgs

/** i.e. ?POST=%s */
val NavGraphScreen.routeArgsFormat: String
    get() = argNames
        ?.joinToString("&") { "${it}=%s" }
        ?.let { "?$it" } ?: ""

/** i.e. ?POST={POST} */
val NavGraphScreen.routeArgs: String
    get() = argNames?.let { arguments ->
        String.format(routeArgsFormat, *arguments.map { "{${it}}" }.toTypedArray())
    } ?: ""

/** i.e. ?POST=[post-json] */
fun NavGraphScreen.routeArgs(args: List<String>?): String {
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

val NavGraphScreen.namedArgs: List<NamedNavArgument>
    get() = argNames?.map { stringArg(it) } ?: emptyList()

val NavGraphScreen.deepLinks: List<NavDeepLink>
    get() = emptyList()


// region transitions
val NavGraphScreen.enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)
    get() = {
        slideIntoContainer(
            animationSpec = screenTransitionAnimationSpec,
            towards = enterTransitionDirection(
                exitScreenOrdinal = targetState.arguments.argOrdinal,
                enterScreenOrdinal = initialState.arguments.argOrdinal,
            )
        )
    }

val NavGraphScreen.exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)
    get() = {
        slideOutOfContainer(
            animationSpec = screenTransitionAnimationSpec,
            towards = exitTransitionDirection(
                exitScreenOrdinal = targetState.arguments.argOrdinal,
                enterScreenOrdinal = initialState.arguments.argOrdinal,
            )
        )
    }

private val screenTransitionAnimationSpec = tween<IntOffset>(200, easing = LinearEasing)

private val Bundle?.argOrdinal get() = this?.getString(ScreenArgKeys.ORDINAL.name)?.toIntOrNull() ?: 0

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
// endregion transitions