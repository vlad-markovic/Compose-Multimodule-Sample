/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_test

import kotlinx.coroutines.flow.StateFlow
import org.junit.jupiter.api.Assertions.assertEquals

fun <T> StateFlow<T>.assertValueEquals(other: T?) {
    assertEquals(other, this.value)
}
