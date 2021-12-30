package com.vladmarkovic.sample.shared_test

import androidx.lifecycle.LiveData
import org.junit.jupiter.api.Assertions.assertEquals

fun <T> LiveData<T>.assertValueEquals(other: T?) {
    assertEquals(other, this.value)
}
