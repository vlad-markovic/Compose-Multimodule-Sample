/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.core.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch
import java.util.Optional

fun <T> Flow<T>.collectIn(coroutineScope: CoroutineScope, onEach: (T) -> Unit) =
    coroutineScope.launch {
        collect { onEach(it) }
    }

fun <T> MutableStateFlow<T>.update(value: T?) {
    if (value != null && this.value != value) this.value = value
}

fun <T> MutableStateFlow<T?>.updateNullable(value: T?) {
    if (this.value != value) this.value = value
}

fun <T> MutableStateFlow<T>.update(value: Optional<T>?) {
    if (value != null && value.isPresent) {
        update(value.get())
    }
}

fun <T> MutableStateFlow<T?>.updateNullable(value: Optional<T>?) {
    if (value != null) {
        if (value.isPresent) {
            update(value.get())
        } else {
            if (this.value != null) this.value = null
        }
    }
}

@ExperimentalCoroutinesApi
fun <T> MutableSharedFlow<T>.withMissedReplayed(millis: Long = 100): SharedFlow<T> = this.onSubscription {
    // For ensuring to be able to emit even from init (before collection is set up)
    replayCache.forEach { emit(it) }
    // allow only emitted up to specified time before subscription to be cached.
    delay(millis)
    resetReplayCache()
}
