/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.settings_presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vladmarkovic.sample.settings_presentation.main.SettingsMainScreenComposer
import com.vladmarkovic.sample.settings_presentation.screen_two.SettingsTwoScreenComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposerSelector
import com.vladmarkovic.sample.shared_presentation.composer.ScreensNavGraph
import com.vladmarkovic.sample.shared_presentation.di.ProviderViewModel
import com.vladmarkovic.sample.shared_domain.screen.SettingsScreen
import com.vladmarkovic.sample.shared_presentation.util.setComposeContentView
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

/** [ScreenComposerSelector] holding [SettingsScreen]s. */
@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setComposeContentView {
            ScreensNavGraph<SettingsScreensComposerSelector, SettingsScreensHolderProvider>()
        }
    }
}

@ViewModelScoped
class SettingsScreensComposerSelector @Inject constructor(
    private val settingsMainScreenComposer: SettingsMainScreenComposer,
    private val settingsTwoScreenComposer: SettingsTwoScreenComposer
) : ScreenComposerSelector<SettingsScreen> {
    override val allScreens: List<SettingsScreen> = SettingsScreen.entries
    override val SettingsScreen.screenComposer: ScreenComposer<*>
        get() = when (this) {
            SettingsScreen.MAIN -> settingsMainScreenComposer
            SettingsScreen.SECOND -> settingsTwoScreenComposer
        }
}

@HiltViewModel
class SettingsScreensHolderProvider @Inject constructor(
    selector: SettingsScreensComposerSelector
) : ProviderViewModel<SettingsScreensComposerSelector>(selector)
