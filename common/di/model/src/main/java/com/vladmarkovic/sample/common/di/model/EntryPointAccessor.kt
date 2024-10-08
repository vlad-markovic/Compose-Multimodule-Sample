/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.di.model

/**
 * [AppEntryPointAccessor] implementation wrapper enabling getting entry points statically,
 * without having to pass application context.
 */
object EntryPointAccessor : AppEntryPointAccessor {

    private lateinit var entryPointAccessor: AppEntryPointAccessor

    fun setupWith(accessor: AppEntryPointAccessor) {
        entryPointAccessor = accessor
    }

    override fun <T: EntryPoint> fromApplication(entryPoint: Class<T>): T =
        entryPointAccessor.fromApplication(entryPoint)
}
