/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import androidx.lifecycle.Observer

/**
 * Use to avoid forced unwrapping [it?.let{...] if not expecting nulls,
 * when observing LiveData values.
 */
class NonNullObserver<T>(private val onNewData: (T) -> Unit) : Observer<T?> {
    override fun onChanged(data: T?) {
        data?.let { onNewData(it) }
    }
}
