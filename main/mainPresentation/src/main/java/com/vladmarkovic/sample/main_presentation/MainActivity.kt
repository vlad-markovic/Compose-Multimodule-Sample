/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.main_presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderComposer
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabNavViewModelFactory
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabNavigable
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabNavigableComposeHolder
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.CovidScreen
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.PostsScreen
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.PostsScreen.FEED_SCREEN
import com.vladmarkovic.sample.shared_presentation.ui.model.MainBottomTab
import com.vladmarkovic.sample.shared_presentation.util.setComposeContentView
import com.vladmarkovic.sample.shared_presentation.util.tabNavViewModels
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/** Main holder activity, holding composers for Composable-s. */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), TabNavigableComposeHolder<MainScreen, MainBottomTab> {

    @Inject
    override lateinit var tabNavViewModelFactory: TabNavViewModelFactory<MainScreen, MainBottomTab>

    override val tabNav: TabNavigable<MainScreen, MainBottomTab> by tabNavViewModels()

    override val initialScreen: MainScreen = FEED_SCREEN

    override val tabs: List<MainBottomTab> = MainBottomTab.values().asList()

    @Inject
    lateinit var postsScreenHolderComposer: ScreenHolderComposer<PostsScreen>

    @Inject
    lateinit var covidTabComposer: ScreenHolderComposer<CovidScreen>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setComposeContentView {
            MainContent()
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun tabComposer(tab: MainBottomTab): ScreenHolderComposer<MainScreen> =
        when (tab) {
            MainBottomTab.POSTS_TAB -> postsScreenHolderComposer as ScreenHolderComposer<MainScreen>
            MainBottomTab.COVID_TAB -> covidTabComposer as ScreenHolderComposer<MainScreen>
        }
}
