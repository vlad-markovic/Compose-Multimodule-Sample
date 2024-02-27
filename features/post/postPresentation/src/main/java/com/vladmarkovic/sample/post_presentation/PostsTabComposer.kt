/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation

import com.vladmarkovic.sample.post_presentation.feed.FeedScreenComposer
import com.vladmarkovic.sample.post_presentation.post.PostScreenComposer
import com.vladmarkovic.sample.shared_domain.log.Lumber
import com.vladmarkovic.sample.shared_presentation.composer.CurrentScreenManager
import com.vladmarkovic.sample.shared_presentation.composer.CurrentScreenMonitor
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderType
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.PostsScreen
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.PostsScreen.FEED_SCREEN
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.PostsScreen.POST_SCREEN
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class PostsTabComposer @Inject constructor(
    private val feedScreenComposer: FeedScreenComposer,
    private val postScreenComposer: PostScreenComposer
) : ScreenHolderComposer<PostsScreen>,
    CurrentScreenMonitor<PostsScreen> by CurrentScreenManager(PostsScreen.entries) {

    override val type: ScreenHolderType = ScreenHolderType.INITIAL_TAB

    init {
        Lumber.e("PostsTabComposer currentScreen ${currentScreen.value}")
    }

    override fun composer(screen: PostsScreen): ScreenComposer {
        Lumber.e("PostsTab resolve composer for $screen")
        return when (screen) {
            FEED_SCREEN -> feedScreenComposer
            POST_SCREEN -> postScreenComposer
            else -> throw IllegalArgumentException("Unhandled screen ${currentScreen.value}")
        }
    }
}
