/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.settings_presentation.screen_two

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.composer.BackScreenComposer
import com.vladmarkovic.sample.shared_presentation.composer.ContentArgs
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.screen.SettingsScreen
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/** Defines Compose UI and elements for second settings screen. */
class SettingsTwoScreenComposer @Inject constructor() : BackScreenComposer<BriefActionViewModel>() {

    override val screenTitle: StateFlow<StrOrRes> = titleFromStr("Settings Two").asStateFlow()

    override val screen: Screen = SettingsScreen.SECOND

    @Composable
    override fun viewModel(contentArgs: ContentArgs): BriefActionViewModel =
        actionViewModel<BriefActionViewModel>(contentArgs)

    @Composable
    override fun Content(contentArgs: ContentArgs, viewModel: BriefActionViewModel) {
        super.Content(contentArgs, viewModel)
        Text("This is Settings Two")
    }
}
