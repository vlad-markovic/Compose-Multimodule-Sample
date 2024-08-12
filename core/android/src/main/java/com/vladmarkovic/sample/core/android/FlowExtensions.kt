/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.core.android

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.vladmarkovic.sample.core.coroutines.collectIn
import kotlinx.coroutines.flow.Flow

fun <T> Flow<T>.collectWith(lifecycleOwner: LifecycleOwner, onEach: (T) -> Unit) =
    collectWith(lifecycleOwner.lifecycle, onEach)

fun <T> Flow<T>.collectWith(lifecycle: Lifecycle, onEach: (T) -> Unit) =
    collectIn(lifecycle.coroutineScope, onEach)
