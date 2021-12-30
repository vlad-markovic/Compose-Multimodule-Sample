package com.vladmarkovic.sample.feed_presentation.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vladmarkovic.sample.feed_presentation.model.PostItem
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme
import com.vladmarkovic.sample.shared_presentation.ui.theme.Dimens
import com.vladmarkovic.sample.shared_presentation.util.padding
import kotlinx.coroutines.flow.Flow

@Composable
fun FeedScreen(
    loading: State<Boolean>,
    posts: Flow<List<PostItem>>,
    error: State<String?>,
    onRefresh: () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(loading.value),
        onRefresh = onRefresh
    ) {
        error.value?.let { Error(it) }
            ?: PostList(posts.collectAsState(emptyList()))
    }
}

@Composable
private fun Error(error: String) {
    Text(
        modifier = Modifier.fillMaxWidth().padding(Dimens.m),
        text = error,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun PostList(_posts: State<List<PostItem>>) {
    LazyColumn {
        val posts by _posts
        items(posts.size) { index ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.m * 2, vertical = Dimens.m),
                elevation = Dimens.m / 2,
                shape = AppTheme.shapes.large
            ) {
                Column {
                    Text(
                        modifier = Modifier.padding(padding = Dimens.m, bottom = (Dimens.m / 2)),
                        text = posts[index].title,
                        style = AppTheme.typography.h6
                    )
                    Text(
                        modifier = Modifier.padding(padding = Dimens.m, top = (Dimens.m / 2)),
                        text = posts[index].content
                    )
                }
            }
        }
    }
}
