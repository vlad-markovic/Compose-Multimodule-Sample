package com.vladmarkovic.sample

import android.app.Application
import com.vladmarkovic.sample.navigation.AppNavigation
import com.vladmarkovic.sample.shared_presentation.navigation.Navigation
import com.vladmarkovic.sample.shared_presentation.navigation.NavigationProvider
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application(), NavigationProvider {

    override val navigation: Navigation = AppNavigation

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
