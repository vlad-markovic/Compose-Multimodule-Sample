package com.vladmarkovic.sample.core.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> onMain(block: suspend CoroutineScope.() -> T): T =
    withContext(Dispatchers.Main, block)
