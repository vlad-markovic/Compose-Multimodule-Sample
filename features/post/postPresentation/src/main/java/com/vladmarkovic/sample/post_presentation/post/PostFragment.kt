/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.R
import com.vladmarkovic.sample.post_presentation.post.PostViewModel.Companion.POST_ARG_KEY
import com.vladmarkovic.sample.post_presentation.post.compose.PostScreen
import com.vladmarkovic.sample.shared_presentation.compose.AppScreen
import com.vladmarkovic.sample.shared_presentation.navigation.CommonNavigationAction.Back
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.util.actionViewModels
import composeContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostFragment : Fragment() {

    companion object {
        fun newInstance(post: Post) = PostFragment().apply {
            arguments = Bundle().apply { putSerializable(POST_ARG_KEY, post) }
        }
    }

    private val viewModel: PostViewModel by actionViewModels()

    private val upButton by lazy {
        mutableStateOf(UpButton.BackButton { viewModel.navigate(Back) })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = composeContent {
        AppScreen(
            title = stringResource(R.string.post_screen_title),
            up = upButton
        ) {
            PostScreen(
                viewModel.post,
                viewModel.authorResult,
                viewModel::getDetails,
                viewModel::deletePost
            )
        }
    }
}
