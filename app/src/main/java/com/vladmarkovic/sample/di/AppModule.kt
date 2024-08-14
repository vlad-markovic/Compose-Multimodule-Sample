/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.di

import android.content.Context
import com.vladmarkovic.sample.connectivity.AppNetworkConnectivity
import com.vladmarkovic.sample.nav.TopNavHandler
import com.vladmarkovic.sample.shared_domain.AppSystem
import com.vladmarkovic.sample.shared_domain.connectivity.NetworkConnectivity
import com.vladmarkovic.sample.shared_presentation.navigation.TopNavigationActionHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSystem(): AppSystem = object : AppSystem {
        override val currentMillis: Long = System.currentTimeMillis()
    }

    @Singleton
    @Provides
    fun provideNetworkConnectivity(@ApplicationContext appContext: Context): NetworkConnectivity =
        AppNetworkConnectivity(appContext)

    @Singleton
    @Provides
    fun provideTopNavHandler(): TopNavigationActionHandler = TopNavHandler()
}
