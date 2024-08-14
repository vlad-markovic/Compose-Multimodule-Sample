/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.core.android

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.vladmarkovic.sample.core.coroutines.collectIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

inline fun <T> Flow<T>.collectWith(
    lifecycleOwner: LifecycleOwner,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    crossinline onEach: suspend CoroutineScope.(T) -> Unit
) =
    collectWith(lifecycleOwner.lifecycle, context, start, onEach)

inline fun <T> Flow<T>.collectWith(
    lifecycle: Lifecycle,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    crossinline onEach: suspend CoroutineScope.(T) -> Unit
) =
    collectIn(lifecycle.coroutineScope, context, start, onEach)
