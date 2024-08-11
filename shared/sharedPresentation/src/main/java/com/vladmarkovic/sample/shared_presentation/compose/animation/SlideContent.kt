package com.vladmarkovic.sample.shared_presentation.compose.animation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import com.vladmarkovic.sample.shared_domain.log.Lumber
import com.vladmarkovic.sample.shared_presentation.screen.screenStackOrdinal

object DefaultScreenTransition {
    val enterDirection = SlideDirection.Start
    const val duration = 200
    val easing = LinearEasing
    val animationSpec: FiniteAnimationSpec<IntOffset> = tween(duration, easing = easing)
    val transition = SlideTransition(animationSpec)
}

data class SlideTransition(
    val animationSpec: FiniteAnimationSpec<IntOffset>,
    val offsetMultiplier: Float = 1f,
    val offsetExtra: Int = 0
)

val SlideDirection.opposite: SlideDirection get() = when (this) {
    SlideDirection.Down -> SlideDirection.Up
    SlideDirection.Up -> SlideDirection.Down
    SlideDirection.Right -> SlideDirection.Left
    SlideDirection.Left -> SlideDirection.Right
    SlideDirection.Start -> SlideDirection.End
    SlideDirection.End -> SlideDirection.Start
    else -> throw IllegalArgumentException("Illegal AnimatedContentTransitionScope.SlideDirection $this to get opposite.")
}

/**
 * Slide version for [Comparable], to slide towards specified [increaseDirection] when comparable increases
 * and in [SlideDirection.opposite] when decreases.
 *
 * On equals there is no animation.
 * Overriding the equals of a class specified to pass as [target] it can be controlled when the animation happens.
 *
 * If both [slideInTransition] and [slideOutTransition] are null, it results in no animation.
 */
@SuppressLint("ModifierParameter")
@Composable
fun <T : Comparable<T>> SlideContent(
    target: T,
    increaseDirection: SlideDirection,
    slideInTransition: SlideTransition?,
    slideOutTransition: SlideTransition? = slideInTransition,
    modifier: Modifier = Modifier,
    extraEnterTransition: EnterTransition? = null,
    extraExitTransition: ExitTransition? = null,
    contentAlignment: Alignment = Alignment.TopStart,
    label: String = "SlideContent",
    contentKey: (targetState: T) -> Any? = { it },
    content: @Composable AnimatedVisibilityScope.(T) -> Unit
) {
    val transitionSpec: AnimatedContentTransitionScope<T>.() -> ContentTransform = {
        slideTransform(slideInTransition, slideOutTransition, extraEnterTransition, extraExitTransition, increaseDirection)
    }
    AnimatedContent(target, modifier, { transitionSpec(this) }, contentAlignment, label, contentKey, content)
}

/**
 * If both [slideInTransition] and [slideOutTransition] are null, it results in no animation.
 */
@SuppressLint("ModifierParameter")
@Composable
fun <T> SlideContent(
    target: T,
    direction: SlideDirection,
    slideInTransition: SlideTransition?,
    slideOutTransition: SlideTransition? = slideInTransition,
    modifier: Modifier = Modifier,
    extraEnterTransition: EnterTransition? = null,
    extraExitTransition: ExitTransition? = null,
    contentAlignment: Alignment = Alignment.TopStart,
    label: String = "SlideContent",
    contentKey: (targetState: T) -> Any? = { it },
    content: @Composable AnimatedVisibilityScope.(T) -> Unit
) {
    val transitionSpec: AnimatedContentTransitionScope<T>.() -> ContentTransform = {
        slideTransform(direction, slideInTransition, slideOutTransition, extraEnterTransition, extraExitTransition)
    }
    AnimatedContent(target, modifier, transitionSpec, contentAlignment, label, contentKey, content)
}


fun <T: Comparable<T>> AnimatedContentTransitionScope<T>.slideTransform(
    slideInTransition: SlideTransition?,
    slideOutTransition: SlideTransition?,
    extraEnterTransition: EnterTransition?,
    extraExitTransition: ExitTransition?,
    increaseDirection: SlideDirection,
): ContentTransform = slideTransform(
    towards = if (targetState >= initialState) increaseDirection else increaseDirection.opposite,
    slideInTransition = slideInTransition,
    slideOutTransition = slideOutTransition,
    extraEnterTransition = extraEnterTransition,
    extraExitTransition = extraExitTransition
)

internal fun <T> AnimatedContentTransitionScope<T>.slideTransform(
    towards: SlideDirection,
    slideInTransition: SlideTransition?,
    slideOutTransition: SlideTransition?,
    extraEnterTransition: EnterTransition?,
    extraExitTransition: ExitTransition?,
): ContentTransform =
    when {
        slideInTransition == null -> EnterTransition.None
        extraEnterTransition == null -> slideIn(towards, slideInTransition)
        else -> slideIn(towards, slideInTransition) + extraEnterTransition
    } togetherWith when {
        slideOutTransition == null -> ExitTransition.None
        extraExitTransition == null -> slideOut(towards, slideOutTransition)
        else -> slideOut(towards, slideOutTransition) + extraExitTransition
    }

internal fun <T> AnimatedContentTransitionScope<T>.slideIn(
    towards: SlideDirection,
    transition: SlideTransition
): EnterTransition = slideIntoContainer(towards, transition.animationSpec) { length ->
    offset(length, transition.offsetMultiplier, transition.offsetExtra)
}

internal fun <T> AnimatedContentTransitionScope<T>.slideOut(
    towards: SlideDirection,
    transition: SlideTransition,
): ExitTransition = slideOutOfContainer(towards, transition.animationSpec) { length ->
    offset(length, transition.offsetMultiplier, transition.offsetExtra)
}

private fun offset(length: Int, offsetMultiplier: Float, offsetExtra: Int) =
    (length * offsetMultiplier).toInt() + offsetExtra

// region slide transition
fun AnimatedContentTransitionScope<NavBackStackEntry>.slideEnterTransition(
    direction: SlideDirection = slideDirection
): EnterTransition =
    slideIntoContainer(animationSpec = DefaultScreenTransition.animationSpec, towards = direction)
        .also { log("enter $direction") }

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideExitTransition(
    direction: SlideDirection = slideDirection
): ExitTransition =
    slideOutOfContainer(animationSpec = DefaultScreenTransition.animationSpec, towards = direction)
        .also { log("exit $direction") }

fun AnimatedContentTransitionScope<NavBackStackEntry>.slidePopEnterTransition(
    direction: SlideDirection = slidePopDirection
): EnterTransition =
    slideIntoContainer(animationSpec = DefaultScreenTransition.animationSpec, towards = direction)
        .also { log("popEnter $direction") }

fun AnimatedContentTransitionScope<NavBackStackEntry>.slidePopExitTransition(
    direction: SlideDirection = slidePopDirection
): ExitTransition =
    slideOutOfContainer(animationSpec = DefaultScreenTransition.animationSpec, towards = direction)
        .also { log("popExit $direction") }

private val AnimatedContentTransitionScope<NavBackStackEntry>.slideDirection
    get() = if (inSameStack) SlideDirection.Down else interStackSlideDirection

private val AnimatedContentTransitionScope<NavBackStackEntry>.interStackSlideDirection
    get() =
        if (initialState.arguments.screenStackOrdinal < targetState.arguments.screenStackOrdinal) {
            SlideDirection.Start
        } else {
            SlideDirection.End
        }

private val AnimatedContentTransitionScope<NavBackStackEntry>.slidePopDirection
    get() = if (inSameStack) SlideDirection.Up else SlideDirection.End

private val AnimatedContentTransitionScope<NavBackStackEntry>.inSameStack
    get() = initialState.destination.parent?.route == targetState.destination.parent?.route

private val AnimatedContentTransitionScope<NavBackStackEntry>.isFirstScreen
    get() = targetState.destination.route == targetState.destination.parent?.route
// endregion slide transition

private fun AnimatedContentTransitionScope<NavBackStackEntry>.log(from: String) {
    Lumber.e("$from, inSameStack: $inSameStack ${initialState.destination.route} ${targetState.destination.route}")
    Lumber.e("$from, ${initialState.arguments.screenStackOrdinal} ${targetState.arguments.screenStackOrdinal}")
}
