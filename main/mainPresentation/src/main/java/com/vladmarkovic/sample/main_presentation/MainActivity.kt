/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.main_presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderComposer
import com.vladmarkovic.sample.shared_presentation.navigation.NavigableComposeHolder
import com.vladmarkovic.sample.shared_presentation.screen.PostsScreen
import com.vladmarkovic.sample.shared_presentation.screen.PostsScreen.FEED_SCREEN
import com.vladmarkovic.sample.shared_presentation.util.setComposeContentView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/** Main holder activity, holding composers for Composable-s. */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigableComposeHolder {

    override val startDestination: String = FEED_SCREEN.name

    @Inject
    lateinit var postsScreenHolderComposer: ScreenHolderComposer<PostsScreen>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setComposeContentView {
            MainContent()
        }
    }

    override fun NavGraphBuilder.navGraph(navController: NavHostController) {
        with(postsScreenHolderComposer) {
            composeNavGraph(navController)
        }
    }

    @Composable
    override fun TopBar(scaffoldState: ScaffoldState) {
        with(postsScreenHolderComposer) {
            ComposeTopBar()
        }
    }
}
