/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun <T> Flow<T>.collectWith(lifecycleOwner: LifecycleOwner, onEach: (T) -> Unit) =
    collectWith(lifecycleOwner.lifecycle, onEach)

fun <T> Flow<T>.collectWith(lifecycle: Lifecycle, onEach: (T) -> Unit) =
    collectIn(lifecycle.coroutineScope, onEach)

fun <T> Flow<T>.collectIn(coroutineScope: CoroutineScope, onEach: (T) -> Unit) =
    coroutineScope.launch {
        collect { onEach(it) }
    }

val <T> StateFlow<T>.safeValue: T @Composable get() = collectAsStateWithLifecycle().value
