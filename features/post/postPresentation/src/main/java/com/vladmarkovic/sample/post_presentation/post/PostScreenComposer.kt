/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.post

import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.post_presentation.R
import com.vladmarkovic.sample.post_presentation.post.compose.PostScreen
import com.vladmarkovic.sample.shared_presentation.compose.AnimateSlide
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenArgs
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.PostsScreen
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton.BackButton
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.isScreenVisible
import com.vladmarkovic.sample.shared_presentation.util.safeValue
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class PostScreenComposer @Inject constructor() : ScreenComposer<PostViewModel>() {

    override val screen: Screen = PostsScreen.POST_SCREEN

    @Composable
    override fun viewModel(args: ScreenArgs): PostViewModel =
        actionViewModel<PostViewModel>(args.bubbleUp)

    @Composable
    override fun Content(args: ScreenArgs, viewModel: PostViewModel) {
        super.Content(args, viewModel)

        SetupScreen(
            args.screenSetup,
            change(
                topBarChange = topBarChange(
                    title = StrOrRes.res(R.string.post_screen_title),
                    upButton = BackButton(viewModel),
                )
            )
        )

        AnimateSlide(args.navController.isScreenVisible(screen.name), -1) {
            PostScreen(
                viewModel.post,
                viewModel.authorResult.safeValue,
                viewModel::getDetails,
                viewModel::deletePost
            )
        }
    }
}
