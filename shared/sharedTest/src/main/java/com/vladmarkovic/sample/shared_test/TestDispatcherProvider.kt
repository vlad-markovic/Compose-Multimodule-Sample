/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_test

import com.vladmarkovic.sample.shared_domain.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@ExperimentalCoroutinesApi
class TestDispatcherProvider(
    override val main: CoroutineDispatcher = UnconfinedTestDispatcher(),
    override val default: CoroutineDispatcher = main,
    override val io: CoroutineDispatcher = main,
    override val unconfined: CoroutineDispatcher = main
) : DispatcherProvider
