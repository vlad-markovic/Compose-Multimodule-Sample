/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_domain.util

import com.vladmarkovic.sample.shared_domain.DispatcherProvider
import com.vladmarkovic.sample.shared_domain.connectivity.NetworkConnectivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


inline fun NetworkConnectivity.doOnMainOnConnectionChange(
    coroutineScope: CoroutineScope,
    dispatchers: DispatcherProvider,
    crossinline doOnConnectionChange: (Boolean) -> Unit
) {
    coroutineScope.launch(dispatchers.io) {
        connectionState.collect { connected ->
            withContext(dispatchers.main) {
                doOnConnectionChange(connected)
            }
        }
    }
}
