/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.screen

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument


/** Builds a [NamedNavArgument] of type String for a given [argName]. */
fun stringArg(argName: String, default: String) =
    navArgument(argName) {
        type = NavType.StringType
        nullable = false
        defaultValue = default
    }

internal fun joinArgs(argNamesWithArg: Map<String, String>): String =
    argNamesWithArg.map { "${it.key}=${it.value}" }.joinToString("&")
