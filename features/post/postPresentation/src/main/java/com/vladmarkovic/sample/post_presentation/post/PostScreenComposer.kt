/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.post

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.vladmarkovic.sample.post_presentation.R
import com.vladmarkovic.sample.post_presentation.post.compose.PostScreen
import com.vladmarkovic.sample.shared_presentation.compose.AnimateSlide
import com.vladmarkovic.sample.shared_presentation.composer.ContentArgs
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposer
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.PostsScreen
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton.BackButton
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.safeValue
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@ActivityRetainedScoped
class PostScreenComposer @Inject constructor() : ScreenComposer() {

    override var upButton: MutableStateFlow<UpButton?> = MutableStateFlow(null)

    override val screenTitle: StateFlow<StrOrRes> = titleFromRes(R.string.post_screen_title).asStateFlow()

    override val screen: Screen = PostsScreen.POST_SCREEN

    @Composable
    override fun Content(contentArgs: ContentArgs) {
        val viewModel: PostViewModel = actionViewModel(contentArgs)

        LaunchedEffect(contentArgs) { upButton.value = BackButton(viewModel) }

        AnimateSlide(contentArgs.navController.isScreenVisible, -1) {
            PostScreen(
                viewModel.post,
                viewModel.authorResult.safeValue,
                viewModel::getDetails,
                viewModel::deletePost
            )
        }
    }
}
