/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.screen

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import com.vladmarkovic.sample.shared_domain.screen.NavGraphScreen
import com.vladmarkovic.sample.shared_presentation.navigation.ToNavGraphScreen

val ToNavGraphScreen.routeWithArgs: String get() = screen.name + screen.routeArgs(args, stackOrdinal)

fun NavGraphScreen.routeData(
    extraArgsNames: Collection<String> = emptySet(),
    deepLinks: Collection<NavDeepLink> = emptySet(),
) =
    ScreenRouteData(routeWithPlaceholders(extraArgsNames), namedArgs(extraArgsNames), deepLinks.toList())

/** i.e. POSTS?SCREEN=POST_SCREEN&STACK_ORDINAL=0&POST=[post-json] */
private fun NavGraphScreen.routeWithPlaceholders(extraArgsNames: Collection<String>): String =
    name + routePlaceholderArgs(extraArgsNames)

/** i.e. ?SCREEN={SCREEN}&STACK_ORDINAL={STACK_ORDINAL} */
private fun NavGraphScreen.routePlaceholderArgs(extraArgsNames: Collection<String>): String =
    "?" + joinArgs((defaultArgNames + extraArgsNames).mapToSelf())

/** i.e. ?POST=[post-json] */
private fun NavGraphScreen.routeArgs(
    screenArgs: Map<String, String>?,
    stackOrdinal: Int? = null
): String =
    if (screenArgs == null) {
        joinArgs(defaultArgs(stackOrdinal))
    } else {
        joinArgs(defaultArgs(stackOrdinal) + screenArgs)
    }.let { "?$it" }

private fun NavGraphScreen.namedArgs(extraArgsNames: Collection<String>): List<NamedNavArgument> =
    ((defaultArgNames + extraArgsNames).mapAllWithValue("")).map { stringArg(it.key, it.value) }

private val NavGraphScreen.defaultArgNames: Set<String> get() = setOf(DefaultScreenArgNames.STACK_ORDINAL.name)

/** Map of arg names and default values. For default args, the actual value is the same as the default value. */
private fun NavGraphScreen.defaultArgs(
    stackOrdinal: Int? = null
): Map<String, String> = stackOrdinal?.let { stackOrdinal ->
    mapOf(DefaultScreenArgNames.STACK_ORDINAL.name to stackOrdinal.toString())
} ?: emptyMap()

private fun Collection<String>.mapWithValues(values: Collection<String>?): Map<String, String> {
    if (values?.size != size) {
        throw IllegalArgumentException("$values size ${values?.size } should match $this size ${this.size}")
    }
    val v = values.toList()
    return mapIndexed { index, key -> key to v[index] }.toMap()
}

private fun Collection<String>.mapAllWithValue(value: String): Map<String, String> = associate { key -> key to value }
private fun Collection<String>.mapToSelf(): Map<String, String> = associate { key -> key to "{$key}" }
