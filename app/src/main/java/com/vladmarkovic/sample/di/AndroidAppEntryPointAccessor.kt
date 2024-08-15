/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.di

import android.content.Context
import com.vladmarkovic.sample.common.di.model.AppEntryPointAccessor
import com.vladmarkovic.sample.common.di.model.EntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/** [AppEntryPointAccessor] implementation, getting entry points using [EntryPointAccessors] */
@Singleton
class AndroidAppEntryPointAccessor @Inject constructor(
    @ApplicationContext private val appContext: Context
) : AppEntryPointAccessor {

    override fun <T : EntryPoint> fromApplication(entryPoint: Class<T>): T =
        EntryPointAccessors.fromApplication(appContext, entryPoint)
}
