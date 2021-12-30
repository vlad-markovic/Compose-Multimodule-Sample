package com.vladmarkovic.sample.post_presentation.feed.compose

import android.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.text.style.TextAlign
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vladmarkovic.sample.post_presentation.feed.model.FeedPostItem
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme
import com.vladmarkovic.sample.shared_presentation.ui.theme.Dimens
import com.vladmarkovic.sample.shared_presentation.util.padding
import kotlinx.coroutines.flow.Flow
import kotlin.math.sqrt
import kotlin.random.Random

@Composable
fun FeedScreen(
    loading: State<Boolean>,
    posts: Flow<List<FeedPostItem>>,
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.m),
        text = error,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun PostList(_posts: State<List<FeedPostItem>>) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = Dimens.m)
    ) {
        val posts by _posts
        items(posts.size) { index ->
            val r = rememberSaveable { randomRgbInt() }
            val g = rememberSaveable { randomRgbInt() }
            val b = rememberSaveable { randomRgbInt() }
            val hsp = rememberSaveable { sqrt(0.299 * r.sqr + 0.587 * g.sqr + 0.114 * b.sqr) }
            val textColor = if (hsp > 130) ComposeColor.Black else ComposeColor.White

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.m * 2, vertical = Dimens.m),
                elevation = Dimens.m / 2,
                shape = AppTheme.shapes.large,
                backgroundColor = ComposeColor(Color.argb(255, r, g, b))
            ) {
                Column {
                    Text(
                        modifier = Modifier.padding(padding = Dimens.m, bottom = (Dimens.m / 2)),
                        text = posts[index].title,
                        style = AppTheme.typography.h6,
                        color = textColor
                    )
                    Text(
                        modifier = Modifier.padding(padding = Dimens.m, top = (Dimens.m / 2)),
                        text = posts[index].content,
                        color = textColor
                    )
                }
            }
        }
    }
}

private fun randomRgbInt(): Int = Random.nextInt(256)

private val Int.sqr: Int get() = this * this
