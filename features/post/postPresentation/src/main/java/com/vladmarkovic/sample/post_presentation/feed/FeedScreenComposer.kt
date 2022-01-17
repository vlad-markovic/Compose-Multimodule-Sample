/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.vladmarkovic.sample.post_presentation.R
import com.vladmarkovic.sample.post_presentation.feed.compose.FeedScreen
import com.vladmarkovic.sample.post_presentation.navigation.ToPostScreen
import com.vladmarkovic.sample.shared_domain.model.DataSource.REMOTE
import com.vladmarkovic.sample.shared_presentation.compose.AnimateSlide
import com.vladmarkovic.sample.shared_presentation.composer.ContentArgs
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposer
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.PostsScreen
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.drawer.DrawerItem
import com.vladmarkovic.sample.shared_presentation.ui.drawer.defaultDrawerItems
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class FeedScreenComposer @Inject constructor() : ScreenComposer() {

    override val screenTitle: State<StrOrRes> = titleFromRes(R.string.feed_screen_title)

    override val screen: Screen = PostsScreen.FEED_SCREEN

    override val upButton: MutableState<UpButton> = mutableStateOf(UpButton.DrawerButton {})

    override val drawerItems: State<List<DrawerItem>> get() = _drawerItems
    private val _drawerItems: MutableState<List<DrawerItem>> = mutableStateOf(emptyList())

    @Composable
    override fun Content(contentArgs: ContentArgs) {
        val viewModel: FeedViewModel = actionViewModel(contentArgs)

        upButton.value = UpButton.DrawerButton(viewModel)

        _drawerItems.value = rememberSaveable { defaultDrawerItems(viewModel) }

        AnimateSlide(contentArgs.navController.isScreenVisible) {
            FeedScreen(
                loading = viewModel.loading,
                posts = viewModel.posts,
                error = viewModel.error,
                onRefresh = { viewModel.refreshPosts(REMOTE) },
                onPostClick = { viewModel.navigate(ToPostScreen(it)) }
            )
        }
    }
}
