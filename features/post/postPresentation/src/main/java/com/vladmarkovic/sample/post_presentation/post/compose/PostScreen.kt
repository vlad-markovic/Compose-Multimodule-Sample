package com.vladmarkovic.sample.post_presentation.post.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vladmarkovic.sample.post_domain.model.Author
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.R
import com.vladmarkovic.sample.shared_presentation.compose.Error
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme
import com.vladmarkovic.sample.shared_presentation.ui.theme.Dimens
import com.vladmarkovic.sample.shared_presentation.util.padding

@Composable
fun PostScreen(
    post: Post,
    _authorResult: State<Result<Author>?>,
    onFetchAuthor: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(Dimens.m)
            .verticalScroll(scrollState)
    ) {
        val authorResult by _authorResult

        PostInfo(post)

        if (authorResult == null) {
            LoadingIndicator()
        } else {
            when {
                authorResult?.isFailure == true -> {
                    Error(stringResource(R.string.error_on_author_fetch)) {
                        onFetchAuthor()
                    }
                }
                authorResult?.isSuccess == true -> {
                    AuthorInfo(authorResult!!.getOrNull()!!)
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.PostInfo(post: Post) {
    Text(
        modifier = Modifier
            .align(Alignment.Start)
            .padding(padding = Dimens.m, bottom = (Dimens.m / 2)),
        text = post.title,
        style = AppTheme.typography.h6
    )
    BodyText(post.content)
}

@Composable
private fun ColumnScope.AuthorInfo(author: Author) {
    BodyText(author.name, Modifier.align(Alignment.Start))
    BodyText(author.email)
}

@Composable
private fun BodyText(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.then(Modifier.padding(padding = Dimens.m, top = (Dimens.m / 2))),
        text = text
    )
}

@Composable
private fun ColumnScope.LoadingIndicator() {
    CircularProgressIndicator(
        Modifier
            .padding(Dimens.m)
            .align(Alignment.CenterHorizontally)
    )
}
