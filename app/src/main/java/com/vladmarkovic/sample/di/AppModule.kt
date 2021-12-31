package com.vladmarkovic.sample.di

import com.vladmarkovic.sample.shared_domain.AppSystem
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
}