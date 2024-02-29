/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.settings_presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.vladmarkovic.sample.settings_presentation.main.SettingsMainScreenComposer
import com.vladmarkovic.sample.settings_presentation.screen_two.SettingsTwoScreenComposer
import com.vladmarkovic.sample.shared_presentation.composer.CurrentScreenManager
import com.vladmarkovic.sample.shared_presentation.composer.CurrentScreenMonitor
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderType
import com.vladmarkovic.sample.shared_presentation.navigation.NavigableComposeHolder
import com.vladmarkovic.sample.shared_presentation.screen.SettingsScreen
import com.vladmarkovic.sample.shared_presentation.util.setComposeContentView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/** [ScreenHolderComposer] holding [SettingsScreen]s. */
@AndroidEntryPoint
class SettingsActivity : AppCompatActivity(),
    NavigableComposeHolder<SettingsScreen>,
    ScreenHolderComposer<SettingsScreen>,
    CurrentScreenMonitor<SettingsScreen> by CurrentScreenManager(SettingsScreen.entries) {

    override val type: ScreenHolderType = ScreenHolderType.STANDALONE

    @Inject
    lateinit var settingsMainScreenComposer: SettingsMainScreenComposer

    @Inject
    lateinit var settingsTwoScreenComposer: SettingsTwoScreenComposer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setComposeContentView {
            MainContent()
        }
    }

    override fun NavGraphBuilder.navGraph(
        navController: NavHostController,
        scaffoldState: ScaffoldState,
        mainScope: CoroutineScope
    ) {
        composeNavGraph(navController, scaffoldState, mainScope)
    }

    override fun composer(screen: SettingsScreen): ScreenComposer<*> = when (screen) {
        SettingsScreen.MAIN -> settingsMainScreenComposer
        SettingsScreen.SECOND -> settingsTwoScreenComposer
    }

    /** Delegate composing of the TopAppBar to the [ScreenComposer]. */
    @Composable
    override fun TopBar(navController: NavHostController) {
        super.ComposeTopBar(navController)
    }

    /** Delegate composing of the Drawer to the [ScreenComposer]. */
    @Composable
    override fun Drawer(scaffoldState: ScaffoldState, mainScope: CoroutineScope) {
        super.ComposeDrawer(scaffoldState, mainScope)
    }
}
