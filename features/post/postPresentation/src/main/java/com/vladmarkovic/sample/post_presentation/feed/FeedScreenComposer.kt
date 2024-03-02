/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.feed

import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.post_presentation.R.string.feed_screen_title
import com.vladmarkovic.sample.post_presentation.feed.compose.FeedScreen
import com.vladmarkovic.sample.post_presentation.navigation.ToPostScreen
import com.vladmarkovic.sample.shared_domain.model.DataSource.REMOTE
import com.vladmarkovic.sample.shared_presentation.briefaction.navigate
import com.vladmarkovic.sample.shared_presentation.compose.AnimateSlide
import com.vladmarkovic.sample.shared_presentation.compose.ScaffoldChange
import com.vladmarkovic.sample.shared_presentation.composer.StackContentArgs
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabInitialScreenComposer
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.PostsScreen
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.drawer.defaultDrawerItems
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.safeValue
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class FeedScreenComposer @Inject constructor() : TabInitialScreenComposer<FeedViewModel> {

    override val screen: Screen = PostsScreen.FEED_SCREEN

    @Composable
    override fun viewModel(stackContentArgs: StackContentArgs): FeedViewModel =
        actionViewModel<FeedViewModel>(stackContentArgs.bubbleUp)

    @Composable
    override fun Content(
        stackContentArgs: StackContentArgs,
        screenSetup: (ScaffoldChange) -> Unit,
        viewModel: FeedViewModel
    ) {
        super.Content(stackContentArgs, screenSetup, viewModel)

        SetupScreen(
            screenSetup,
            change(
                topBarChange = topBarChange(
                    title = StrOrRes.res(feed_screen_title),
                    upButton = UpButton.DrawerButton(viewModel),
                ),
                drawerItems = defaultDrawerItems(viewModel)
            )
        )

        AnimateSlide(stackContentArgs.navController.isScreenVisible) {
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
