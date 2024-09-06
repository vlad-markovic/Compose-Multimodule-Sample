package com.vladmarkovic.sample.compose

import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.common.mv.action.Action
import com.vladmarkovic.sample.common.navigation.screen.compose.content.ComposeScreenContentResolver
import com.vladmarkovic.sample.settings_presentation.main.SettingsMainScreen
import com.vladmarkovic.sample.settings_presentation.screen_two.SettingsTwoScreen
import com.vladmarkovic.sample.shared_domain.screen.SettingsScreen
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SettingsScreenContentResolver @Inject constructor(): ComposeScreenContentResolver<SettingsScreen> {

    @Composable
    override fun SettingsScreen.Content(bubbleUp: (Action) -> Unit) = when (this) {
        SettingsScreen.MAIN -> SettingsMainScreen(bubbleUp)
        SettingsScreen.SECOND -> SettingsTwoScreen(bubbleUp)
    }
}
