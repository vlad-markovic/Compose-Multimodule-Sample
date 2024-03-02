/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation.tabbed

import androidx.activity.ComponentActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.compose.ScaffoldChange
import com.vladmarkovic.sample.shared_presentation.composer.ContentArgs
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderType
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.util.collectWith
import com.vladmarkovic.sample.shared_presentation.util.navigate
import kotlinx.coroutines.flow.drop

/** For decorating an Activity to give it Tabbed Compose Navigation functionality. */
interface TabNavigableComposeHolder<S : Screen, T : Tab<S>> {

    val tabNavViewModelFactory: TabNavViewModelFactory<S, T>

    val tabNav: TabNavigable<S, T> // TabNavViewModel

    val tabs: List<T>

    val mainTab: T get() = tabs.first()

    val initialScreen: S get() = mainTab.initialScreen

    val initialDestination: String get() = mainTab.name

    fun tabComposer(tab: T): ScreenHolderComposer<S>

    fun NavGraphBuilder.navGraph(
        contentArgs: ContentArgs,
        scaffoldChange: (ScaffoldChange) -> Unit,
        bubbleUp: (ScreenHolderType, BriefAction) -> Unit
    ) {
        tabNav.setupWith(contentArgs.navController)
        tabs.forEach { tab ->
            navigation(
                startDestination = tab.initialScreen.name,
                route = tab.name
            ) {
                with(tabComposer(tab)) {
                    composeNavGraph(contentArgs, scaffoldChange, bubbleUp)
                }
            }
        }
    }

    fun setCurrentTab(tab: T) {
        tabNav.navigate(tab)
    }

    fun TabNavigable<S, T>.setupWith(navController: NavController) {
        with(this@TabNavigableComposeHolder as ComponentActivity) {
            tab.drop(1).collectWith(this) {
                navController.navigate(it)
            }
        }
    }
}
