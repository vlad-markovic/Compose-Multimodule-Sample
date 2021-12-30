package com.vladmarkovic.sample.feed_presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vladmarkovic.sample.feed_presentation.compose.FeedScreen
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme
import composeContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : Fragment() {

    companion object {
        fun newInstance() = FeedFragment()
    }

    private val viewModel: FeedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = composeContent {
        AppTheme {
            // A surface container using the 'background' color from the theme
            Surface(color = MaterialTheme.colors.background) {
                FeedScreen(
                    loading = viewModel.loading,
                    posts = viewModel.posts,
                    error = viewModel.error,
                    onRefresh = viewModel::refreshPosts,
                )
            }
        }
    }
}