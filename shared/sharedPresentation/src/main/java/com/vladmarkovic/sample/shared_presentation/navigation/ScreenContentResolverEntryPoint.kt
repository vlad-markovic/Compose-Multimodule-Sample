package com.vladmarkovic.sample.shared_presentation.navigation

import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.vladmarkovic.sample.shared_domain.di.EntryPoint

@Suppress("unused")
@dagger.hilt.EntryPoint
@InstallIn(SingletonComponent::class)
interface ScreenContentResolverEntryPoint : EntryPoint {
    fun screenContentResolver(): ScreenContentResolver
}
