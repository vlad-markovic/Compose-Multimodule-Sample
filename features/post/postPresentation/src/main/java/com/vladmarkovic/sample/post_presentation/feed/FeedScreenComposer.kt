/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.feed

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.post_presentation.R.string.feed_screen_title
import com.vladmarkovic.sample.post_presentation.feed.compose.FeedScreen
import com.vladmarkovic.sample.post_presentation.navigation.ToPostScreen
import com.vladmarkovic.sample.shared_domain.model.DataSource.REMOTE
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.briefaction.navigate
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposer
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.PostsScreen
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.drawer.defaultDrawerItems
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.safeValue
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.util.Optional
import javax.inject.Inject

@ActivityRetainedScoped
class FeedScreenComposer @Inject constructor() : ScreenComposer<FeedViewModel>() {

    override val screen: Screen = PostsScreen.FEED_SCREEN

    override fun topBarChange(viewModel: FeedViewModel): Optional<@Composable (() -> Unit)> =
        defaultTopBarChange(StrOrRes.res(feed_screen_title), upButton = UpButton.DrawerButton(viewModel))

    override fun drawerChange(viewModel: FeedViewModel): Optional<@Composable (ColumnScope.() -> Unit)> =
        defaultDrawerChange(defaultDrawerItems(viewModel))

    @Composable
    override fun viewModel(bubbleUp: (BriefAction) -> Unit): FeedViewModel =
        actionViewModel<FeedViewModel>(bubbleUp)

    @Composable
    override fun Content(viewModel: FeedViewModel) {
        FeedScreen(
            loading = viewModel.loading.safeValue,
            posts = viewModel.posts.safeValue,
            error = viewModel.error.safeValue,
            onRefresh = { viewModel.refreshPosts(REMOTE) },
            onPostClick = { viewModel.navigate(ToPostScreen(it)) }
        )
    }
}
