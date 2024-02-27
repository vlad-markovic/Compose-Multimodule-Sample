/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.settings_presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.briefaction.navigate
import com.vladmarkovic.sample.shared_presentation.composer.BackScreenComposer
import com.vladmarkovic.sample.shared_presentation.composer.ContentArgs
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.screen.SettingsScreen
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

/** Defines Compose UI and elements for main settings screen. */
class SettingsMainScreenComposer @Inject constructor() : BackScreenComposer() {

    override val screenTitle: MutableStateFlow<StrOrRes> = titleFromStr("Settings")

    override val screen: Screen = SettingsScreen.MAIN

    @Composable
    override fun Content(contentArgs: ContentArgs) {
        super.Content(contentArgs)

        val viewModel: BriefActionViewModel = actionViewModel(contentArgs)

        Column (Modifier.padding(16.dp)) {
            Button({ viewModel.navigate(ToSecondSettingsScreen) }) {
                Text("Go To Settings Two")
            }
        }
    }
}
