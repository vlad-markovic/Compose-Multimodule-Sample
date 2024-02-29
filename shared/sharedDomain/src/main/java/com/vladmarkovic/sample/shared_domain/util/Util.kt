/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_domain.util

import com.vladmarkovic.sample.shared_domain.DispatcherProvider
import com.vladmarkovic.sample.shared_domain.connectivity.NetworkConnectivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Convenience extension for function which don't return Unit
 * for when statement needing the common return to be Unit.
 */
@Suppress("unused", "UnusedReceiverParameter")
fun <T> T.ignoreReturn() = Unit

@Suppress("UNUSED_PARAMETER", "RedundantNullableReturnType")
fun doNothing(vararg args: Any): Any? = Unit

inline fun NetworkConnectivity.doOnMainOnConnectionChange(
    coroutineScope: CoroutineScope,
    dispatchers: DispatcherProvider,
    crossinline doOnConnectionChange: (Boolean) -> Unit
) {
    coroutineScope.launch(dispatchers.io) {
        connectionState.collect { connected ->
            withContext(dispatchers.main) {
                doOnConnectionChange(connected)
            }
        }
    }
}

inline fun <T, R : Comparable<R>> Iterable<T>.sortedBy(
    ascending: Boolean,
    crossinline selector: (T) -> R?
): List<T> =
    if (ascending) sortedBy { selector(it) }
    else sortedByDescending { selector(it) }
