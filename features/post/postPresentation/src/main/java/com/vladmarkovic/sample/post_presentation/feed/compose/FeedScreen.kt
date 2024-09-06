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
import com.vladmarkovic.sample.common.mv.action.Action
import com.vladmarkovic.sample.common.mv.action.compose.actionViewModel
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.model.ScaffoldData
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.model.TopBarData
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.model.UpButton
import com.vladmarkovic.sample.core.kotlin.doNothing
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.R
import com.vladmarkovic.sample.post_presentation.R.string.error_on_posts_fetch
import com.vladmarkovic.sample.post_presentation.feed.FeedEvent
import com.vladmarkovic.sample.post_presentation.feed.FeedState
import com.vladmarkovic.sample.post_presentation.feed.FeedViewModel
import com.vladmarkovic.sample.shared_domain.model.DataSource
import com.vladmarkovic.sample.shared_domain.tab.MainBottomTab
import com.vladmarkovic.sample.shared_presentation.compose.components.Error
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultTopBar
import com.vladmarkovic.sample.shared_presentation.ui.model.MainDrawerItem
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme
import com.vladmarkovic.sample.shared_presentation.ui.theme.Dimens


@Composable
fun FeedScreen(
    bubbleUp: (Action) -> Unit,
    viewModel: FeedViewModel = actionViewModel<FeedViewModel>(bubbleUp)
) {
    LaunchedEffect(Unit) {
        bubbleUp(ScaffoldData(drawerItems = defaultDrawerItems(viewModel)))
    }

    Column(Modifier.fillMaxSize()) {
        DefaultTopBar(
            TopBarData(
                StrOrRes.res(R.string.feed_screen_title),
                UpButton.DrawerButton { viewModel.onEvent(FeedEvent.OnOpenDrawer) }
            )
        )

        FeedScreen(
            state = viewModel.state.lifecycleAwareValue,
            onRefresh = { viewModel.onEvent(FeedEvent.OnRefreshPosts(DataSource.REMOTE)) },
            onPostClick = { viewModel.onEvent(FeedEvent.OnPostTapped(it)) }
        )
    }
}

@Composable
private fun FeedScreen(
    state: FeedState,
    onRefresh: () -> Unit,
    onPostClick: (Post) -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(state == FeedState.Loading),
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        when(state) {
            is FeedState.Error -> Error(stringResource(error_on_posts_fetch), onRefresh)
            is FeedState.Posts -> PostList(state.posts, onPostClick)
            is FeedState.Loading -> doNothing()
        }
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

fun defaultDrawerItems(viewModel: FeedViewModel) = listOf(
    MainDrawerItem.ItemPostsTab {
        viewModel.onEvent(FeedEvent.OnCloseDrawer)
        viewModel.onEvent(FeedEvent.OnTabTapped(MainBottomTab.POSTS_TAB))
    },
    MainDrawerItem.ItemCovidTab {
        viewModel.onEvent(FeedEvent.OnCloseDrawer)
        viewModel.onEvent(FeedEvent.OnTabTapped(MainBottomTab.COVID_TAB))
    },
    MainDrawerItem.ItemToast {
        viewModel.onEvent(FeedEvent.OnCloseDrawer)
        viewModel.onEvent(FeedEvent.OnShowToast("A Toast"))
    },
    MainDrawerItem.ItemSettings {
        viewModel.onEvent(FeedEvent.OnCloseDrawer)
        viewModel.onEvent(FeedEvent.OnSettingsTapped)
    }
)
