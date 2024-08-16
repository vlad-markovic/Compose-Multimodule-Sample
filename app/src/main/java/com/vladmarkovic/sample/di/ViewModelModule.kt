/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.di

import com.vladmarkovic.sample.compose.MainScreenContentResolver
import com.vladmarkovic.sample.covid_data.CovidInfoApiService
import com.vladmarkovic.sample.covid_domain.CovidInfoRepo
import com.vladmarkovic.sample.post_data.AuthorDataRepository
import com.vladmarkovic.sample.post_data.PostDataRepository
import com.vladmarkovic.sample.post_domain.AuthorRepository
import com.vladmarkovic.sample.post_domain.PostRepository
import com.vladmarkovic.sample.common.navigation.screen.compose.content.ComposeScreenContentResolver
import com.vladmarkovic.sample.compose.SettingsScreenContentResolver
import com.vladmarkovic.sample.shared_domain.screen.MainScreen
import com.vladmarkovic.sample.shared_domain.screen.SettingsScreen
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface ViewModelModule {

    @Binds
    @ViewModelScoped
    fun bindPostRepository(repo: PostDataRepository): PostRepository

    @Binds
    @ViewModelScoped
    fun bindAuthorRepository(repo: AuthorDataRepository): AuthorRepository

    @Binds
    @ViewModelScoped
    fun bindCovidRepository(api: CovidInfoApiService): CovidInfoRepo

    @Binds
    @ViewModelScoped
    fun provideMainScreenContentResolver(resolver: MainScreenContentResolver):
        ComposeScreenContentResolver<MainScreen>

    @Binds
    @ViewModelScoped
    fun provideSettingsScreenContentResolver(resolver: SettingsScreenContentResolver):
        ComposeScreenContentResolver<SettingsScreen>
}
