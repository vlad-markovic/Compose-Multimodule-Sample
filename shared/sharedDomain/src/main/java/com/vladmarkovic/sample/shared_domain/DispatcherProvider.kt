package com.vladmarkovic.sample.shared_domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/** For injecting and replacing with test DispatcherProvider for testing. */
open class DispatcherProvider @Inject constructor() {
    open val main: CoroutineDispatcher = Dispatchers.Main
    open val default = Dispatchers.Default
    open val io = Dispatchers.IO
    open val unconfined = Dispatchers.Unconfined
}
