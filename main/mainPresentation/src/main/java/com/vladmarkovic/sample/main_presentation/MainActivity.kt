/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.main_presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposerSelector
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposerSelectorSelector
import com.vladmarkovic.sample.shared_presentation.composer.TabsNavGraph
import com.vladmarkovic.sample.shared_presentation.di.ProviderViewModel
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.CovidScreen
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.PostsScreen
import com.vladmarkovic.sample.shared_presentation.ui.model.MainBottomTab
import com.vladmarkovic.sample.shared_presentation.util.setComposeContentView
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

/** Main holder activity, holding composers for Composable-s. */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setComposeContentView {
            TabsNavGraph<MainBottomTab, MainBottomTabComposerSelectorSelector, MainBottomTabComposerSelectorSelectorProvider>()
        }
    }
}


@ViewModelScoped
class MainBottomTabComposerSelectorSelector @Inject constructor(
    private val postsScreenComposerSelector: ScreenComposerSelector<PostsScreen>,
    private val covidScreenComposerSelector: ScreenComposerSelector<CovidScreen>
) : ScreenComposerSelectorSelector<MainBottomTab> {
    override val allTabs: List<MainBottomTab> get() = MainBottomTab.entries
    override val MainBottomTab.screenComposerSelector: ScreenComposerSelector<*>
        get() = when (this) {
            MainBottomTab.POSTS_TAB -> postsScreenComposerSelector
            MainBottomTab.COVID_TAB -> covidScreenComposerSelector
        }
}

@HiltViewModel
class MainBottomTabComposerSelectorSelectorProvider @Inject constructor(
    selector: MainBottomTabComposerSelectorSelector
) : ProviderViewModel<MainBottomTabComposerSelectorSelector>(selector)
