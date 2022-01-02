package com.vladmarkovic.sample.shared_domain.log

import com.vladmarkovic.sample.shared_domain.di.EntryPoint

/** [EntryPoint] for providing [Logger] injection into Objects */
interface LoggerEntryPoint : EntryPoint {
    fun logger(): Logger
}
