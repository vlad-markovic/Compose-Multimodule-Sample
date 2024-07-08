package com.vladmarkovic.sample.shared_presentation.compose.di

import com.vladmarkovic.sample.shared_domain.di.EntryPoint
import com.vladmarkovic.sample.shared_presentation.compose.ScreenContentResolver
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@dagger.hilt.EntryPoint
@InstallIn(SingletonComponent::class)
interface ScreenContentResolverEntryPoint : EntryPoint {
    fun screenContentResolver(): ScreenContentResolver
}