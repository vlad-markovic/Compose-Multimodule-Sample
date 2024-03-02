/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State

fun <T> MutableState<T>.update(state: State<T>) {
    update(state.value)
}

fun <T> MutableState<T>.update(value: T) {
    if (this.value != value) this.value = value
}
