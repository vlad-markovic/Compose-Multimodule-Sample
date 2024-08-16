/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.compose.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import com.vladmarkovic.sample.core.coroutines.update
import com.vladmarkovic.sample.core.coroutines.updateNullable
import kotlinx.coroutines.flow.StateFlow

fun <T> MutableStateFlow<T>.update(state: State<T>) {
    update(state.value)
}

fun <T> MutableStateFlow<T?>.updateNullable(state: State<T?>) {
    updateNullable(state.value)
}

val <T> StateFlow<T>.lifecycleAwareValue: T @Composable get() = collectAsStateWithLifecycle().value
