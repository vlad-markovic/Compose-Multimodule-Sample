/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation

import com.vladmarkovic.sample.post_presentation.feed.FeedScreenComposer
import com.vladmarkovic.sample.post_presentation.post.PostScreenComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposerSelector
import com.vladmarkovic.sample.shared_domain.screen.MainScreen.PostsScreen
import com.vladmarkovic.sample.shared_domain.screen.MainScreen.PostsScreen.FEED_SCREEN
import com.vladmarkovic.sample.shared_domain.screen.MainScreen.PostsScreen.POST_SCREEN
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class PostsScreenComposerSelector @Inject constructor(
    private val feedScreenComposer: FeedScreenComposer,
    private val postScreenComposer: PostScreenComposer
) : ScreenComposerSelector<PostsScreen> {

    override val allScreens: List<PostsScreen> = PostsScreen.entries

    override val PostsScreen.screenComposer: ScreenComposer<*>
        get() = when (this) {
            FEED_SCREEN -> feedScreenComposer
            POST_SCREEN -> postScreenComposer
        }
}
