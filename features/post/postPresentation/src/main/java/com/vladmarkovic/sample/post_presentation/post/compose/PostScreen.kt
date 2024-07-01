/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.post.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.vladmarkovic.sample.post_domain.model.Author
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.R
import com.vladmarkovic.sample.post_presentation.R.string.delete_post_button_label
import com.vladmarkovic.sample.post_presentation.R.string.error_on_author_fetch
import com.vladmarkovic.sample.post_presentation.post.PostViewModel
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.compose.Error
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.ScaffoldData
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme
import com.vladmarkovic.sample.shared_presentation.ui.theme.Dimens
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.padding
import com.vladmarkovic.sample.shared_presentation.util.safeValue
import java.io.IOException

@Composable
fun PostScreen(bubbleUp: (BriefAction) -> Unit) {
    val viewModel = actionViewModel<PostViewModel>(bubbleUp)
    bubbleUp(
        ScaffoldData(
            topBarTitle = StrOrRes.res(R.string.post_screen_title),
            upButton = UpButton.BackButton(viewModel),
        )
    )
    PostScreen(
        viewModel.post,
        viewModel.authorResult.safeValue,
        viewModel::getDetails,
        viewModel::deletePost
    )
}

@Composable
private fun PostScreen(
    post: Post,
    authorResult: Result<Author>?,
    onFetchAuthor: () -> Unit,
    onDeletePost: (Post) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(Dimens.m)
            .verticalScroll(scrollState)
    ) {
        PostInfo(post)

        if (authorResult == null) {
            LoadingIndicator()
        } else {
            when {
                authorResult.isFailure -> {
                    Error(stringResource(error_on_author_fetch)) {
                        onFetchAuthor()
                    }
                }
                authorResult.isSuccess -> {
                    AuthorInfo(authorResult.getOrNull()!!)
                }
            }

            Button(
                modifier = Modifier.padding(Dimens.m),
                onClick = { onDeletePost(post) }
            ) {
                Text(stringResource(delete_post_button_label))
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

// region preview
private val successAuthorResult
    get() = Result.success(
        object : Author {
            override val id: Int = 3
            override val name: String = "Author"
            override val username: String = "Auth"
            override val email: String = "auth@email.com"
        }
    )

private val post
    get() = object: Post {
        override val id: Int = 1
        override val userId: Int = 2
        override val title: String = "Title"
        override val content: String = "Content"
    }

@Preview
@Composable
private fun PreviewSuccessPostScreen() {
    PostScreen(post, successAuthorResult, {}, {})
}

private val failureAuthorResult = Result.failure<Author>(IOException())

@Preview
@Composable
private fun PreviewFailurePostScreen() {
    PostScreen(post, failureAuthorResult, {}, {})
}

@Preview
@Composable
private fun PreviewLoadingPostScreen() {
    PostScreen(post, authorResult = null, {}, {})
}
// endregion preview
