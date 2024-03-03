/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.post

import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.post_presentation.R
import com.vladmarkovic.sample.post_presentation.post.compose.PostScreen
import com.vladmarkovic.sample.shared_presentation.compose.AnimateSlide
import com.vladmarkovic.sample.shared_presentation.compose.ScaffoldChange
import com.vladmarkovic.sample.shared_presentation.composer.StackContentArgs
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.composer.TabScreenComposer
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.PostsScreen
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton.BackButton
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.safeValue
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class PostScreenComposer @Inject constructor() : TabScreenComposer<PostViewModel> {

    override val screen: Screen = PostsScreen.POST_SCREEN

    @Composable
    override fun viewModel(stackContentArgs: StackContentArgs): PostViewModel =
        actionViewModel<PostViewModel>(stackContentArgs.bubbleUp)

    @Composable
    override fun Content(
        stackContentArgs: StackContentArgs,
        screenSetup: (ScaffoldChange) -> Unit,
        viewModel: PostViewModel
    ) {
        super.Content(stackContentArgs, screenSetup, viewModel)

        SetupScreen(screenSetup,
            change(
                topBarChange = topBarChange(
                    title = StrOrRes.res(R.string.post_screen_title),
                    upButton = BackButton(viewModel),
                )
            )
        )

        AnimateSlide(stackContentArgs.navController.isScreenVisible, -1) {
            PostScreen(
                viewModel.post,
                viewModel.authorResult.safeValue,
                viewModel::getDetails,
                viewModel::deletePost
            )
        }
    }
}
