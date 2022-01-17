/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation

import com.vladmarkovic.sample.post_presentation.feed.FeedScreenComposer
import com.vladmarkovic.sample.post_presentation.post.PostScreenComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderType
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.PostsScreen
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.PostsScreen.*
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class PostsScreenHolderComposer @Inject constructor(
    private val feedScreenComposer: FeedScreenComposer,
    private val postScreenComposer: PostScreenComposer
) : ScreenHolderComposer<PostsScreen>() {

    override val type: ScreenHolderType = ScreenHolderType.STANDALONE

    override val allScreens: Array<PostsScreen> = PostsScreen.values()

    override fun composer(screen: PostsScreen): ScreenComposer = when (currentScreen.value) {
        FEED_SCREEN -> feedScreenComposer
        POST_SCREEN -> postScreenComposer
        else -> throw IllegalArgumentException("Unhandled screen ${currentScreen.value}")
    }
}
