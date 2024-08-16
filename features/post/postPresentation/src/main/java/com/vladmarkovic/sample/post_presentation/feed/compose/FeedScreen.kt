/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.feed.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vladmarkovic.sample.common.android.model.StrOrRes
import com.vladmarkovic.sample.common.compose.util.lifecycleAwareValue
import com.vladmarkovic.sample.common.compose.util.padding
import com.vladmarkovic.sample.common.mv.action.compose.actionViewModel
import com.vladmarkovic.sample.common.mv.action.ViewAction
import com.vladmarkovic.sample.common.mv.action.navigate
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.R
import com.vladmarkovic.sample.post_presentation.R.string.error_on_posts_fetch
import com.vladmarkovic.sample.post_presentation.feed.FeedViewModel
import com.vladmarkovic.sample.post_presentation.navigation.ToPostScreen
import com.vladmarkovic.sample.shared_domain.model.DataSource
import com.vladmarkovic.sample.shared_presentation.compose.components.Error
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.model.ScaffoldData
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultTopBar
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.model.TopBarData
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.model.UpButton
import com.vladmarkovic.sample.shared_presentation.ui.model.defaultDrawerItems
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme
import com.vladmarkovic.sample.shared_presentation.ui.theme.Dimens


@Composable
fun FeedScreen(
    bubbleUp: (ViewAction) -> Unit,
    viewModel: FeedViewModel = actionViewModel<FeedViewModel>(bubbleUp)
) {
    LaunchedEffect(Unit) {
        bubbleUp(ScaffoldData(drawerItems = defaultDrawerItems(viewModel)))
    }

    Column(Modifier.fillMaxSize()) {
        DefaultTopBar(
            TopBarData(StrOrRes.res(R.string.feed_screen_title), UpButton.DrawerButton(viewModel))
        )

        FeedScreen(
            loading = viewModel.loading.lifecycleAwareValue,
            posts = viewModel.posts.lifecycleAwareValue,
            error = viewModel.error.lifecycleAwareValue,
            onRefresh = { viewModel.refreshPosts(DataSource.REMOTE) },
            onPostClick = { viewModel.navigate(ToPostScreen(it)) }
        )
    }
}

@Composable
private fun FeedScreen(
    loading: Boolean,
    posts: List<Post>,
    error: Boolean,
    onRefresh: () -> Unit,
    onPostClick: (Post) -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(loading),
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        if (error) Error(stringResource(error_on_posts_fetch), onRefresh)
        else PostList(posts, onPostClick)
    }
}

@Composable
private fun PostList(posts: List<Post>, onPostClick: (Post) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = Dimens.m),
        modifier = Modifier.fillMaxSize()
    ) {
        items(posts.size) { index ->
            val post = posts[index]

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.m * 2, vertical = Dimens.s)
                    .clickable { onPostClick(post) },
                elevation = Dimens.m / 2,
                shape = AppTheme.shapes.large
            ) {
                Column {
                    Text(
                        modifier = Modifier.padding(
                            padding = Dimens.m,
                            bottom = (Dimens.m / 2)
                        ),
                        text = post.title,
                        style = AppTheme.typography.h6
                    )
                    Text(
                        modifier = Modifier.padding(padding = Dimens.m, top = (Dimens.m / 2)),
                        text = post.content,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
