/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.drop

fun <T> LiveData<T>.drop(count: Int): LiveData<T> = asFlow().drop(count).asLiveData()

/** Use for non-nullable LiveData type, to be able to observe only non null values */
fun <T> LiveData<T>.observeNonNull(lifecycleOwner: LifecycleOwner, onNewData: (T) -> Unit) =
    observe(lifecycleOwner, NonNullObserver(onNewData))
