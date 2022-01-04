/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_domain.di

import dagger.hilt.components.SingletonComponent

/** Use only for [SingletonComponent] [dagger.hilt.EntryPoint]-s */
interface AppEntryPointAccessor {

    fun <T: EntryPoint> fromApplication(entryPoint: Class<T>): T
}
