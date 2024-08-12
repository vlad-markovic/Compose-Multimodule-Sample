/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow


fun <T> MutableState<T>.update(state: State<T>) {
    update(state.value)
}

fun <T> MutableState<T>.update(value: T) {
    if (this.value != value) this.value = value
}


val <T> StateFlow<T>.lifecycleAwareValue: T @Composable get() = collectAsStateWithLifecycle().value
