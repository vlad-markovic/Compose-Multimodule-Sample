package com.vladmarkovic.sample.feed_presentation.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vladmarkovic.sample.feed_presentation.model.PostItem
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
    Text(error)
}

@Composable
private fun PostList(_posts: State<List<PostItem>>) {
    LazyColumn {
        val posts by _posts
        items(posts.size) { index ->
            Text(
                posts[index].title,
                Modifier.padding(8.dp),
            )
        }
    }
}
