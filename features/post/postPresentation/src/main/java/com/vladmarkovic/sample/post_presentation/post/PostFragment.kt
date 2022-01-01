package com.vladmarkovic.sample.post_presentation.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.R
import com.vladmarkovic.sample.post_presentation.post.compose.PostScreen
import com.vladmarkovic.sample.shared_presentation.compose.AppScreen
import composeContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostFragment : Fragment() {

    companion object {
        private const val POST_ARG_KEY = "POST_ARG_KEY"

        fun newInstance(post: Post) = PostFragment().apply {
            arguments = Bundle().apply { putSerializable(POST_ARG_KEY, post) }
        }
    }

    private val viewModel: PostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = composeContent {
        AppScreen(stringResource(R.string.post_screen_title)) {
            PostScreen(viewModel.post, viewModel.authorResult, viewModel::getDetails)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDetails(requireArguments().getSerializable(POST_ARG_KEY) as Post)
    }
}
