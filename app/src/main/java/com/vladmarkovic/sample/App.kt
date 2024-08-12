/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample

import android.app.Activity
import android.app.Application
import com.vladmarkovic.sample.common.di.EntryPointAccessor
import com.vladmarkovic.sample.di.AndroidAppEntryPointAccessor
import com.vladmarkovic.sample.shared_presentation.navigation.TopNavigationActionHandler
import com.vladmarkovic.sample.shared_presentation.navigation.ToScreenGroup
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), TopNavigationActionHandler {

    @Inject
    lateinit var appEntryPointAccessor: AndroidAppEntryPointAccessor

    @Inject
    lateinit var topNavigationActionHandler: TopNavigationActionHandler

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        EntryPointAccessor.setupWith(appEntryPointAccessor)
    }

    override fun handleTopScreenNavigationAction(activity: Activity, action: ToScreenGroup) {
        topNavigationActionHandler.handleTopScreenNavigationAction(activity, action)
    }
}
