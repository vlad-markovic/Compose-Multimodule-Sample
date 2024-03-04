/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.feed

import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.post_presentation.R.string.feed_screen_title
import com.vladmarkovic.sample.post_presentation.feed.compose.FeedScreen
import com.vladmarkovic.sample.post_presentation.navigation.ToPostScreen
import com.vladmarkovic.sample.shared_domain.model.DataSource.REMOTE
import com.vladmarkovic.sample.shared_presentation.briefaction.navigate
import com.vladmarkovic.sample.shared_presentation.compose.AnimateSlide
import com.vladmarkovic.sample.shared_presentation.compose.ScreenChange
import com.vladmarkovic.sample.shared_presentation.composer.ScreenArgs
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposer
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.PostsScreen
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.drawer.defaultDrawerItems
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.isScreenVisible
import com.vladmarkovic.sample.shared_presentation.util.safeValue
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class FeedScreenComposer @Inject constructor() : ScreenComposer<FeedViewModel>() {

    override val screen: Screen = PostsScreen.FEED_SCREEN

    @Composable
    override fun viewModel(args: ScreenArgs): FeedViewModel =
        actionViewModel<FeedViewModel>(args.bubbleUp)

    override fun scaffoldChange(viewModel: FeedViewModel): ScreenChange = change(
        topBarChange = topBarChange(
            title = StrOrRes.res(feed_screen_title),
            upButton = UpButton.DrawerButton(viewModel),
        ),
        drawerItems = defaultDrawerItems(viewModel)
    )

    @Composable
    override fun Content(args: ScreenArgs, viewModel: FeedViewModel) {
        super.Content(args, viewModel)

        AnimateSlide(args.navController.isScreenVisible(screen.name)) {
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
