/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.screen

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * A representation of a screen with defined [name] for navigation,
 * and defined [argNames] for arguments it expects, if any.
 */
@Stable
@Immutable
sealed interface Screen {
    val name: String
    val argNames: List<String>? get() = null
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
fun Screen.routeArgs(args: List<String>): String {
    if (args.isEmpty()) return ""
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
