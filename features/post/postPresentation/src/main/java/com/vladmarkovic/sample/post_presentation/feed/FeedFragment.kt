package com.vladmarkovic.sample.post_presentation.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import com.vladmarkovic.sample.post_presentation.R
import com.vladmarkovic.sample.post_presentation.feed.compose.FeedScreen
import com.vladmarkovic.sample.shared_presentation.compose.AppScreen
import com.vladmarkovic.sample.shared_presentation.util.actionViewModels
import composeContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : Fragment() {

    companion object {
        fun newInstance() = FeedFragment()
    }

    private val viewModel: FeedViewModel by actionViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = composeContent {
        AppScreen(stringResource(R.string.feed_screen_title)) {
            FeedScreen(
                loading = viewModel.loading,
                posts = viewModel.posts,
                error = viewModel.error,
                onRefresh = { viewModel.refreshPosts(forceRefresh = true) },
                onPostClick = { viewModel.navigateToPostDetails(it) }
            )
        }
    }
}
