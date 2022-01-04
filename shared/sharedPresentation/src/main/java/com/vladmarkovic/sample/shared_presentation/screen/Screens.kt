/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.screen

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.vladmarkovic.sample.shared_presentation.screen.PostsScreen.ArgKeys.POST

sealed interface Screen {
    val name: String
    val args: List<NamedNavArgument>? get() = null
}

enum class PostsScreen : Screen {
    FEED_SCREEN,
    POST_SCREEN {
        override val args: List<NamedNavArgument> = listOf(stringArg(POST.name))
    };

    enum class ArgKeys {
        POST
    }
}

val Screen.route: String get() = name + routeArgs

val Screen.routeArgsFormat: String get() = args
    ?.joinToString("&") { "${it.name}=%s" }
    ?.let { "?$it" } ?: ""

val Screen.routeArgs: String get() = args?.let { arguments ->
    String.format(routeArgsFormat, *arguments.map { "{${it.name}}" }.toTypedArray())
} ?: ""

fun Screen.routeArgs(args: List<String>): String {
    if (args.isEmpty()) return ""
    if (args.size != this.args?.size ?: 0) throw IllegalArgumentException()
    return String.format(routeArgsFormat, *args.toTypedArray())
}

private fun stringArg(argName: String) =
    navArgument(argName) {
        type = NavType.StringType
        nullable = false
        defaultValue = ""
    }
