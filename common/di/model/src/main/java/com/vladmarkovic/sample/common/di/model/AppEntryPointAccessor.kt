/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.di.model

interface AppEntryPointAccessor {
    fun <T : EntryPoint> fromApplication(entryPoint: Class<T>): T
}
