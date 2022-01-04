/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_data.di

import android.content.Context
import androidx.room.Room
import com.vladmarkovic.sample.post_data.PostApi
import com.vladmarkovic.sample.post_data.PostApiService
import com.vladmarkovic.sample.post_data.PostDao
import com.vladmarkovic.sample.post_data.PostDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PostAppModule {

    @Singleton
    @Provides
    fun providePostDatabase(@ApplicationContext appContext: Context): PostDatabase =
        Room.databaseBuilder(
            appContext,
            PostDatabase::class.java,
            PostDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun providePostDao(database: PostDatabase): PostDao = database.postDao()

    @Singleton
    @Provides
    fun providePostApi(httpClient: HttpClient): PostApi = PostApiService(httpClient)
}
