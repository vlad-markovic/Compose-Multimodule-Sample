package com.vladmarkovic.sample.di

import android.content.Context
import com.vladmarkovic.sample.connectivity.AppNetworkConnectivity
import com.vladmarkovic.sample.log.TimberLogger
import com.vladmarkovic.sample.shared_domain.AppSystem
import com.vladmarkovic.sample.shared_domain.connectivity.NetworkConnectivity
import com.vladmarkovic.sample.shared_domain.log.Logger
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
    fun provideLogger(logger: TimberLogger): Logger = logger
}
