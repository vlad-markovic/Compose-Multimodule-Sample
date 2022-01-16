/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_domain

import kotlinx.coroutines.CoroutineDispatcher

/** For injecting and replacing with test DispatcherProvider for testing. */
interface DispatcherProvider  {
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
    val io: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}
