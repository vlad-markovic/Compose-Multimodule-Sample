package com.vladmarkovic.sample.post_presentation.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Scaffold
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vladmarkovic.sample.post_presentation.R
import com.vladmarkovic.sample.post_presentation.feed.compose.FeedScreen
import com.vladmarkovic.sample.shared_presentation.ui.compose.TopBar
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
            Scaffold(
                topBar = { TopBar(stringResource(R.string.feed_screen_title)) }
            ) {
                val systemUiController = rememberSystemUiController()
                systemUiController.setSystemBarsColor(Color.Black)

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
