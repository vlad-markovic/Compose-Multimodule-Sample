/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.logging

import com.vladmarkovic.sample.common.di.model.EntryPoint

/** [EntryPoint] for providing [Logger] injection into Objects */
interface LoggerEntryPoint : EntryPoint {
    fun logger(): Logger
}
