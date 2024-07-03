/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.screen

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.vladmarkovic.sample.shared_domain.screen.MainScreen
import com.vladmarkovic.sample.shared_domain.screen.Screen


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

