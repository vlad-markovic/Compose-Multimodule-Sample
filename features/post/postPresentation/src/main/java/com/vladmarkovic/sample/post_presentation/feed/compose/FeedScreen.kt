/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.feed.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.R.string.error_on_posts_fetch
import com.vladmarkovic.sample.shared_presentation.compose.Error
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme
import com.vladmarkovic.sample.shared_presentation.ui.theme.Dimens
import com.vladmarkovic.sample.shared_presentation.util.padding

@Composable
fun FeedScreen(
    loading: Boolean,
    posts: List<Post>,
    error: Boolean,
    onRefresh: () -> Unit,
    onPostClick: (Post) -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(loading),
        onRefresh = onRefresh
    ) {
        if (error) Error(stringResource(error_on_posts_fetch), onRefresh)
        else PostList(posts, onPostClick)
    }
}

@Composable
private fun PostList(posts: List<Post>, onPostClick: (Post) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = Dimens.m)
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
