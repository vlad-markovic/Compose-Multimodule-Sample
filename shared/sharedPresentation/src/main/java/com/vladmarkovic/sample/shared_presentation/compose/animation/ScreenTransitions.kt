package com.vladmarkovic.sample.shared_presentation.compose.animation

import android.net.Uri
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import com.vladmarkovic.sample.common.compose.animation.inSameStack
import com.vladmarkovic.sample.shared_presentation.tab.TabArgsNames


object DefaultScreenTransition {
    const val duration = 200
    val easing = LinearEasing
    val floatTween: FiniteAnimationSpec<Float> = tween(duration, easing = easing)
    val intTween: FiniteAnimationSpec<IntOffset> = tween(duration, easing = easing)
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(): EnterTransition =
    if (inSameStack) fadeIn(DefaultScreenTransition.floatTween) + scaleIn(DefaultScreenTransition.floatTween)
    else slideEnterTransition()

fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition =
    if (inSameStack) fadeOut(DefaultScreenTransition.floatTween)
    else slideExitTransition()


fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition(): EnterTransition =
    if (inSameStack) fadeIn(DefaultScreenTransition.floatTween)
    else slidePopEnterTransition()

fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition(): ExitTransition =
    if (inSameStack) fadeOut(DefaultScreenTransition.floatTween) + scaleOut(DefaultScreenTransition.floatTween)
    else slidePopExitTransition()

// region slide transition
fun AnimatedContentTransitionScope<NavBackStackEntry>.slideEnterTransition(
    direction: SlideDirection = slideDirection
): EnterTransition =
    slideIntoContainer(animationSpec = DefaultScreenTransition.intTween, towards = direction)

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideExitTransition(
    direction: SlideDirection = slideDirection
): ExitTransition =
    slideOutOfContainer(animationSpec = DefaultScreenTransition.intTween, towards = direction)

fun AnimatedContentTransitionScope<NavBackStackEntry>.slidePopEnterTransition(
    direction: SlideDirection = slidePopDirection
): EnterTransition =
    slideIntoContainer(animationSpec = DefaultScreenTransition.intTween, towards = direction)

fun AnimatedContentTransitionScope<NavBackStackEntry>.slidePopExitTransition(
    direction: SlideDirection = slidePopDirection
): ExitTransition =
    slideOutOfContainer(animationSpec = DefaultScreenTransition.intTween, towards = direction)

private val AnimatedContentTransitionScope<NavBackStackEntry>.slideDirection
    get() = if (inSameStack) SlideDirection.Down else interStackSlideDirection

private val AnimatedContentTransitionScope<NavBackStackEntry>.interStackSlideDirection
    get() =
        if (initialState.stackOrdinal < targetState.stackOrdinal) SlideDirection.Start
        else SlideDirection.End

private val AnimatedContentTransitionScope<NavBackStackEntry>.slidePopDirection
    get() = if (inSameStack) SlideDirection.Up else SlideDirection.End

val NavBackStackEntry.stackOrdinal: Int
    get() = Uri.parse(destination.parent?.route)
        .getQueryParameter(TabArgsNames.STACK_ORDINAL.name)
        ?.toIntOrNull()
        ?: 0
// endregion slide transition
