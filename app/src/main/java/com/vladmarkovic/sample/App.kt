/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample

import android.app.Application
import com.vladmarkovic.sample.di.AndroidAppEntryPointAccessor
import com.vladmarkovic.sample.shared_domain.di.EntryPointAccessor
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var appEntryPointAccessor: AndroidAppEntryPointAccessor

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        EntryPointAccessor.setupWith(appEntryPointAccessor)
    }
}
