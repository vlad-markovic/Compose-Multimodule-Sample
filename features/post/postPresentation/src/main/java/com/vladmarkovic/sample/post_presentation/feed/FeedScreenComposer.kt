/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.vladmarkovic.sample.post_presentation.R.string.feed_screen_title
import com.vladmarkovic.sample.post_presentation.feed.compose.FeedScreen
import com.vladmarkovic.sample.post_presentation.navigation.ToPostScreen
import com.vladmarkovic.sample.shared_domain.model.DataSource.REMOTE
import com.vladmarkovic.sample.shared_presentation.briefaction.navigate
import com.vladmarkovic.sample.shared_presentation.compose.AnimateSlide
import com.vladmarkovic.sample.shared_presentation.composer.ContentArgs
import com.vladmarkovic.sample.shared_presentation.composer.DrawerScreenComposer
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.PostsScreen
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.drawer.DrawerItem
import com.vladmarkovic.sample.shared_presentation.ui.drawer.defaultDrawerItems
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.safeValue
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@ActivityRetainedScoped
class FeedScreenComposer @Inject constructor() : DrawerScreenComposer<FeedViewModel>() {

    override val screenTitle: StateFlow<StrOrRes> = titleFromRes(feed_screen_title).asStateFlow()

    override val screen: Screen = PostsScreen.FEED_SCREEN

    override val upButton: MutableStateFlow<UpButton> = MutableStateFlow(UpButton.DrawerButton {})

    override val drawerItems: StateFlow<List<DrawerItem>> get() = _drawerItems.asStateFlow()
    private val _drawerItems: MutableStateFlow<List<DrawerItem>> = MutableStateFlow(emptyList())

    @Composable
    override fun viewModel(contentArgs: ContentArgs): FeedViewModel = actionViewModel<FeedViewModel>(contentArgs)

    @Composable
    override fun Content(contentArgs: ContentArgs, viewModel: FeedViewModel) {
        super.Content(contentArgs, viewModel)

        LaunchedEffect(contentArgs) {
            upButton.value = UpButton.DrawerButton(viewModel)
            _drawerItems.value = defaultDrawerItems(viewModel)
        }

        AnimateSlide(contentArgs.navController.isScreenVisible) {
            FeedScreen(
                loading = viewModel.loading.safeValue,
                posts = viewModel.posts.safeValue,
                error = viewModel.error.safeValue,
                onRefresh = { viewModel.refreshPosts(REMOTE) },
                onPostClick = { viewModel.navigate(ToPostScreen(it)) }
            )
        }
    }
}
