package com.vladmarkovic.sample.post_presentation.feed.compose

import android.graphics.Color
import androidx.annotation.IntRange
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.R
import com.vladmarkovic.sample.shared_presentation.compose.Error
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme
import com.vladmarkovic.sample.shared_presentation.ui.theme.Dimens
import com.vladmarkovic.sample.shared_presentation.util.padding
import kotlin.math.sqrt
import kotlin.random.Random
import androidx.compose.ui.graphics.Color as ComposeColor

@Composable
fun FeedScreen(
    loading: State<Boolean>,
    posts: State<List<Post>>,
    error: State<Boolean>,
    onRefresh: () -> Unit,
    onPostClick: (Post) -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(loading.value),
        onRefresh = onRefresh
    ) {
        if (error.value) Error(stringResource(R.string.error_on_posts_fetch), onRefresh)
        else PostList(posts, onPostClick)
    }
}

@Composable
private fun PostList(_posts: State<List<Post>>, onPostClick: (Post) -> Unit) {
    val posts by _posts
    LazyColumn(
        contentPadding = PaddingValues(vertical = Dimens.m)
    ) {
        items(posts.size) { index ->
            val r = rememberSaveable { randomRgbInt() }
            val g = rememberSaveable { randomRgbInt() }
            val b = rememberSaveable { randomRgbInt() }
            val isDarkColor = rememberSaveable { isDarkColor(r, g, b) }
            val textColor = if (isDarkColor) ComposeColor.Black else ComposeColor.White

            val post = posts[index]

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.m * 2, vertical = Dimens.s)
                    .clickable { onPostClick(post) },
                elevation = Dimens.m / 2,
                shape = AppTheme.shapes.large,
                backgroundColor = ComposeColor(Color.argb(255, r, g, b))
            ) {
                Column {
                    Text(
                        modifier = Modifier.padding(padding = Dimens.m, bottom = (Dimens.m / 2)),
                        text = post.title,
                        style = AppTheme.typography.h6,
                        color = textColor
                    )
                    Text(
                        modifier = Modifier.padding(padding = Dimens.m, top = (Dimens.m / 2)),
                        text = post.content,
                        color = textColor,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

private fun randomRgbInt(): Int = Random.nextInt(256)

private fun isDarkColor(
    @IntRange(from = 0, to = 255) r: Int,
    @IntRange(from = 0, to = 255) g: Int,
    @IntRange(from = 0, to = 255) b: Int
): Boolean {
    val hsp = sqrt(0.299 * r.sqr + 0.587 * g.sqr + 0.114 * b.sqr)
    return hsp > 127.5
}

private val Int.sqr: Int get() = this * this
