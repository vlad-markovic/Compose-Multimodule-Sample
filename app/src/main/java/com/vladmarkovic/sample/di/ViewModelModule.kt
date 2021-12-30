package com.vladmarkovic.sample.di

import com.vladmarkovic.sample.feed_data.PostApiService
import com.vladmarkovic.sample.feed_domain.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface ViewModelModule {

    @Binds
    fun bindPostRepository(apiService: PostApiService): PostRepository
}
