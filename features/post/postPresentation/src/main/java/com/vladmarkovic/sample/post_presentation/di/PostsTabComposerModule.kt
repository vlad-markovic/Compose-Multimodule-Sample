package com.vladmarkovic.sample.post_presentation.di

import com.vladmarkovic.sample.post_presentation.PostsScreenComposerSelector
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposerSelector
import com.vladmarkovic.sample.shared_domain.screen.MainScreen.PostsScreen
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
interface PostsTabComposerModule {

    @Binds
    fun bindCovidInfoTabComposer(tabComposer: PostsScreenComposerSelector) : ScreenComposerSelector<PostsScreen>
}
