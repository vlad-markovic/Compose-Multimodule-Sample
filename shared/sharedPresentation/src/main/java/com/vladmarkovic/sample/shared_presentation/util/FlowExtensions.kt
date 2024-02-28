/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProduceStateScope
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun <T> Flow<T>.collectWith(lifecycleOwner: LifecycleOwner, onEach: (T) -> Unit) =
    collectWith(lifecycleOwner.lifecycle, onEach)

fun <T> Flow<T>.collectWith(lifecycle: Lifecycle, onEach: (T) -> Unit) =
    collectIn(lifecycle.coroutineScope, onEach)

fun <T> Flow<T>.collectIn(coroutineScope: CoroutineScope, onEach: (T) -> Unit) =
    coroutineScope.launch {
        collect { onEach(it) }
    }

val <T> StateFlow<T>.safeValue: T @Composable get() = collectAsStateLifecycleAware().value

@Composable
fun <T> StateFlow<T>.safeValue(initial: T): T = collectAsStateLifecycleAware(initial).value


/**
 * Copied from androidx.lifecycle.compose.collectAsStateWithLifecycle
 *
 * Collects values from this [Flow] and represents its latest value via [State] in a
 * lifecycle-aware manner.
 *
 * Every time there would be new value posted into the [Flow] the returned [State] will be updated
 * causing recomposition of every [State.value] usage whenever the [lifecycle] is at
 * least [minActiveState].
 *
 * This [Flow] is collected every time [lifecycle] reaches the [minActiveState] Lifecycle
 * state. The collection stops when [lifecycle] falls below [minActiveState].
 *
 * @sample androidx.lifecycle.compose.samples.FlowCollectAsStateWithLifecycle
 *
 * Warning: [Lifecycle.State.INITIALIZED] is not allowed in this API. Passing it as a
 * parameter will throw an [IllegalArgumentException].
 *
 * @param initialValue The initial value given to the returned [State.value].
 * @param lifecycle [Lifecycle] used to restart collecting `this` flow.
 * @param minActiveState [Lifecycle.State] in which the upstream flow gets collected. The
 * collection will stop if the lifecycle falls below that state, and will restart if it's in that
 * state again.
 * @param context [CoroutineContext] to use for collecting.
 */
@Composable
fun <T> StateFlow<T>.collectAsStateLifecycleAware(
    initialValue: T = value,
    key: Any = this,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext
): State<T> =
    rememberState(initialValue, key) {
        lifecycle.repeatOnLifecycle(minActiveState) {
            if (context == EmptyCoroutineContext) {
                this@collectAsStateLifecycleAware.collect { this@rememberState.value = it }
            } else withContext(context) {
                this@collectAsStateLifecycleAware.collect { this@rememberState.value = it }
            }
        }
    }

/**
 * Copied from androidx.compose.runtime.produceState.
 * Safer version which uses [initialValue] and [keys] to remember produced State.
 *
 * Return an observable [snapshot][androidx.compose.runtime.snapshots.Snapshot] [State] that
 * produces values over time from [keys].
 *
 * [producer] is launched when [produceState] enters the composition and is cancelled when
 * [produceState] leaves the composition. If [keys] change, a running [producer] will be
 * cancelled and re-launched for the new source. [producer] should use [ProduceStateScope.value]
 * to set new values on the returned [State].
 *
 * The returned [State] conflates values; no change will be observable if
 * [ProduceStateScope.value] is used to set a value that is [equal][Any.equals] to its old value,
 * and observers may only see the latest value if several values are set in rapid succession.
 *
 * [produceState] may be used to observe either suspending or non-suspending sources of external
 * data, for example:
 *
 * @sample androidx.compose.runtime.samples.ProduceState
 *
 * @sample androidx.compose.runtime.samples.ProduceStateAwaitDispose
 */
@Composable
fun <T> rememberState(
    initialValue: T,
    vararg keys: Any?,
    producer: suspend ProduceStateScope<T>.() -> Unit
): State<T> {
    val result = remember(keys = keys) { mutableStateOf(initialValue) }
    (LaunchedEffect(keys = keys) {
        ProduceStateScopeImpl(result, coroutineContext).producer()
    })
    return result
}


/**
 * Copied from androidx.compose.runtime.ProduceStateScopeImpl
 * Receiver scope implementation for use with [rememberState].
 */
private class ProduceStateScopeImpl<T>(
    state: MutableState<T>,
    override val coroutineContext: CoroutineContext
) : ProduceStateScope<T>, MutableState<T> by state {
    override suspend fun awaitDispose(onDispose: () -> Unit): Nothing {
        try {
            suspendCancellableCoroutine<Nothing> { }
        } finally {
            onDispose()
        }
    }
}
