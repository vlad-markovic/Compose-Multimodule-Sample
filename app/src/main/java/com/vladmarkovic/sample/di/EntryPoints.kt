package com.vladmarkovic.sample.di

import com.vladmarkovic.sample.shared_domain.log.LoggerEntryPoint
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppLoggerEntryPoint : LoggerEntryPoint
