package com.vladmarkovic.sample.shared_presentation.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

/** Use for non-nullable LiveData type, to be able to observe only non null values */
fun <T> LiveData<T>.observeNonNull(lifecycleOwner: LifecycleOwner, onNewData: (T) -> Unit) =
    observe(lifecycleOwner, NonNullObserver(onNewData))
