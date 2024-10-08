/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.navigation.screen.navcomponent.util

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import com.vladmarkovic.sample.common.navigation.screen.model.Screen
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.ScreenRouteData
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.ToNavGraphScreen

val ToNavGraphScreen.routeWithArgs: String get() = screen.name + routeArgs(args)

fun Screen.routeData(
    extraArgsNames: Collection<String> = emptySet(),
    deepLinks: Collection<NavDeepLink> = emptySet(),
) = ScreenRouteData(
    routeWithPlaceholders(extraArgsNames),
    namedArgs(extraArgsNames),
    deepLinks.toList()
)

/** i.e. POST?POST=[post-json] */
private fun Screen.routeWithPlaceholders(extraArgsNames: Collection<String>): String =
    name + routePlaceholderArgs(extraArgsNames)

/** i.e. ?POST={POST} */
private fun routePlaceholderArgs(extraArgsNames: Collection<String>): String =
    "?" + joinArgs(extraArgsNames.mapToSelf())

/** i.e. ?POST=[post-json] */
private fun routeArgs(screenArgs: Map<String, String>?): String =
    screenArgs?.let { "?" + joinArgs(screenArgs) }.orEmpty()

private fun namedArgs(extraArgsNames: Collection<String>): List<NamedNavArgument> =
    (extraArgsNames.mapWithValue("")).map { stringArg(it.key, it.value) }

private fun Collection<String>.mapWithValue(value: String): Map<String, String> =
    associate { key -> key to value }

private fun Collection<String>.mapToSelf(): Map<String, String> =
    associate { key -> key to "{$key}" }
