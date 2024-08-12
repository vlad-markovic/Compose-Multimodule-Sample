package com.vladmarkovic.sample.common.compose.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavBackStackEntry

val AnimatedContentTransitionScope<NavBackStackEntry>.inSameStack
    get() = initialState.destination.parent?.route == targetState.destination.parent?.route

val AnimatedContentTransitionScope<NavBackStackEntry>.isFirstScreen
    get() = targetState.destination.route == targetState.destination.parent?.route
