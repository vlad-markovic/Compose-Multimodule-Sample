/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.compose.util

import androidx.compose.runtime.State
import kotlinx.coroutines.flow.MutableStateFlow
import com.vladmarkovic.sample.core.coroutines.update
import com.vladmarkovic.sample.core.coroutines.updateNullable

fun <T> MutableStateFlow<T>.update(state: State<T>) {
    update(state.value)
}

fun <T> MutableStateFlow<T?>.updateNullable(state: State<T?>) {
    updateNullable(state.value)
}
