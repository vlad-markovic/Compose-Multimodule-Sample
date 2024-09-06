/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

@Composable
fun AnimateSlide(
    visible: Boolean,
    reverse: Int = 1,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally { -1 * reverse * it / 4 },
        exit = slideOutHorizontally { reverse * it / 4 },
        content = content
    )
}

@Composable
fun AnimateFade(
    visible: Boolean,
    label: String,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        label = label,
        content = content
    )
}

//@Composable
//fun BackHandler(args: ScreenArgs)  {
//    BackHandler(actionViewModel<ActionViewModel>(args.bubbleUp))
//}
//
//@Composable
//fun <VM> BackHandler(viewModel: VM) where VM : MutableSharedFlow<Action>, VM : ViewModel {
//    LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher?.let { backDispatcher ->
//        val onBack by rememberUpdatedState { viewModel.navigate(CommonNavigationAction.Back) }
//        val backCallback = remember {
//            object : OnBackPressedCallback(enabled = true) {
//                override fun handleOnBackPressed() { onBack() }
//            }
//        }
//        val lifecycleOwner = LocalLifecycleOwner.current
//        OnLifecycleEvent { lifecycleEvent ->
//            when (lifecycleEvent) {
//                Lifecycle.Event.ON_START -> backDispatcher.addCallback(lifecycleOwner, backCallback)
//                Lifecycle.Event.ON_STOP -> backCallback.remove()
//                else -> doNothing()
//            }
//        }
//        DisposableEffect(lifecycleOwner, backDispatcher) {
//            onDispose { backCallback.remove() }
//        }
//    } ?: Lumber.e("No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner")
//}

@Composable
fun OnStart(onStart: () -> Unit) {
    OnLifecycleEvent { event ->
        if (event == Lifecycle.Event.ON_START) onStart()
    }
}

@Composable
fun OnLifecycleEvent(onEach: (Lifecycle.Event) -> Unit) {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val currentOnEach by rememberUpdatedState(newValue = onEach)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event -> currentOnEach(event) }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}
