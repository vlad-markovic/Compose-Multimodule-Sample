package com.vladmarkovic.sample.shared_presentation.compose.animation

import android.net.Uri
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavBackStackEntry
import com.vladmarkovic.sample.shared_presentation.tab.TabArgsNames

val AnimatedContentTransitionScope<NavBackStackEntry>.inSameStack
    get() = initialState.destination.parent?.route == targetState.destination.parent?.route

val AnimatedContentTransitionScope<NavBackStackEntry>.isFirstScreen
    get() = targetState.destination.route == targetState.destination.parent?.route

val NavBackStackEntry.stackOrdinal: Int
    get() = Uri.parse(destination.parent?.route)
        .getQueryParameter(TabArgsNames.STACK_ORDINAL.name)
        ?.toIntOrNull()
        ?: 0
