package com.vladmarkovic.sample.post_presentation.di

import com.vladmarkovic.sample.post_presentation.PostsTabComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderComposer
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.PostsScreen
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
interface PostsTabComposerModule {

    @Binds
    fun bindCovidInfoTabComposer(tabComposer: PostsTabComposer) : ScreenHolderComposer<PostsScreen>
}
