/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample

import android.app.Application
import com.vladmarkovic.sample.di.AndroidAppEntryPointAccessor
import com.vladmarkovic.sample.navigation.AppNavigation
import com.vladmarkovic.sample.shared_domain.di.EntryPointAccessor
import com.vladmarkovic.sample.shared_presentation.navigation.Navigation
import com.vladmarkovic.sample.shared_presentation.navigation.NavigationProvider
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), NavigationProvider {

    @Inject
    lateinit var appEntryPointAccessor: AndroidAppEntryPointAccessor

    override val navigation: Navigation = AppNavigation

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        EntryPointAccessor.setupWith(appEntryPointAccessor)
    }
}
