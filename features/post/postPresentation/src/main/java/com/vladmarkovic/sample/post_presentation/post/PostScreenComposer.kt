package com.vladmarkovic.sample.post_presentation.post

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import com.vladmarkovic.sample.post_presentation.R
import com.vladmarkovic.sample.post_presentation.post.compose.PostScreen
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposer
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton.BackButton
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class PostScreenComposer @Inject constructor() : ScreenComposer() {

    override var upButton: MutableState<UpButton?> = mutableStateOf(null)

    override val screenTitle: State<StrOrRes> = titleFromRes(R.string.post_screen_title)

    @Composable
    override fun Content(navController: NavHostController) {
        val viewModel: PostViewModel = actionViewModel(navController)

        upButton.value = BackButton(viewModel)

        PostScreen(
            viewModel.post,
            viewModel.authorResult,
            viewModel::getDetails,
            viewModel::deletePost
        )
    }
}
