/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.di

interface AppEntryPointAccessor {
    fun <T: EntryPoint> fromApplication(entryPoint: Class<T>): T
}
