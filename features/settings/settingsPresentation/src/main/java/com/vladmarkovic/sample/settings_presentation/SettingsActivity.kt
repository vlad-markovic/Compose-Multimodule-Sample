/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.settings_presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import com.vladmarkovic.sample.settings_presentation.main.SettingsMainScreenComposer
import com.vladmarkovic.sample.settings_presentation.screen_two.SettingsTwoScreenComposer
import com.vladmarkovic.sample.shared_presentation.compose.ScreensHolder
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderType
import com.vladmarkovic.sample.shared_presentation.screen.SettingsScreen
import com.vladmarkovic.sample.shared_presentation.util.setComposeContentView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/** [ScreenHolderComposer] holding [SettingsScreen]s. */
@AndroidEntryPoint
class SettingsActivity : AppCompatActivity(), ScreenHolderComposer<SettingsScreen> {

    override val type: ScreenHolderType = ScreenHolderType.STANDALONE

    override val allScreens: List<SettingsScreen> = SettingsScreen.entries

    @Inject
    lateinit var settingsMainScreenComposer: SettingsMainScreenComposer

    @Inject
    lateinit var settingsTwoScreenComposer: SettingsTwoScreenComposer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setComposeContentView {
            val firstScreen = allScreens.first()
            ScreensHolder(firstScreen) { args, modifier ->
                NavHost(
                    navController = args.navController,
                    startDestination = firstScreen.name,
                    modifier = modifier,
                ) {
                    composeNavGraph(args)
                }
            }
        }
    }

    override fun composer(screen: SettingsScreen): ScreenComposer<*> = when (screen) {
        SettingsScreen.MAIN -> settingsMainScreenComposer
        SettingsScreen.SECOND -> settingsTwoScreenComposer
    }
}
