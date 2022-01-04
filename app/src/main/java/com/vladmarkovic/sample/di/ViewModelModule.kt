/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.di

import com.vladmarkovic.sample.post_data.AuthorDataRepository
import com.vladmarkovic.sample.post_data.PostDataRepository
import com.vladmarkovic.sample.post_domain.AuthorRepository
import com.vladmarkovic.sample.post_domain.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface ViewModelModule {

    @Binds
    fun bindPostRepository(repo: PostDataRepository): PostRepository

    @Binds
    fun bindAuthorRepository(repo: AuthorDataRepository): AuthorRepository
}
