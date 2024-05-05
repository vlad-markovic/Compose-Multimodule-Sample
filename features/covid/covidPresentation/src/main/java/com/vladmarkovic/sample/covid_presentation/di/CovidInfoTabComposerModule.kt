/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation.di

import com.vladmarkovic.sample.covid_presentation.CovidScreenComposerSelector
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposerSelector
import com.vladmarkovic.sample.shared_domain.screen.MainScreen.CovidScreen
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
interface CovidInfoTabComposerModule {

    @Binds
    fun bindCovidInfoTabComposer(tabComposer: CovidScreenComposerSelector) : ScreenComposerSelector<CovidScreen>
}
