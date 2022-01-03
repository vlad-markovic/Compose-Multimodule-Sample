package com.vladmarkovic.sample.di

import com.vladmarkovic.sample.post_presentation.PostsScreenHolderComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderComposer
import com.vladmarkovic.sample.shared_presentation.screen.PostsScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object RetainedModule {

    @ActivityRetainedScoped
    @Provides
    fun providePostsScreenHolderComposer(holderComposer: PostsScreenHolderComposer):
            ScreenHolderComposer<PostsScreen> = holderComposer
}
