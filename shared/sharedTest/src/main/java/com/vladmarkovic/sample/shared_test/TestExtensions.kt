/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_test

import androidx.compose.runtime.State
import androidx.lifecycle.LiveData
import org.junit.jupiter.api.Assertions.assertEquals

fun <T> LiveData<T>.assertValueEquals(other: T?) {
    assertEquals(other, this.value)
}

fun <T> State<T>.assertValueEquals(other: T?) {
    assertEquals(other, this.value)
}
