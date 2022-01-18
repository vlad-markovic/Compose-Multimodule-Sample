/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.vladmarkovic.sample.post_presentation.feed.FeedScreenComposer
import com.vladmarkovic.sample.post_presentation.post.PostScreenComposer
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
) : ScreenHolderComposer<PostsScreen> {

    override val type: ScreenHolderType = ScreenHolderType.INITIAL_TAB

    override val allScreens: Array<PostsScreen> = PostsScreen.values()

    override val currentScreen: MutableState<PostsScreen> = mutableStateOf(allScreens.first())

    override fun composer(screen: PostsScreen): ScreenComposer = when (currentScreen.value) {
        FEED_SCREEN -> feedScreenComposer
        POST_SCREEN -> postScreenComposer
        else -> throw IllegalArgumentException("Unhandled screen ${currentScreen.value}")
    }
}
