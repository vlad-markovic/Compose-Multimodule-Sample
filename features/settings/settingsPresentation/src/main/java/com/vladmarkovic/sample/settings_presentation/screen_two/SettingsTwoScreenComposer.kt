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
class SettingsTwoScreenComposer @Inject constructor() : BackScreenComposer() {

    override val screenTitle: StateFlow<StrOrRes> = titleFromStr("Settings Two").asStateFlow()

    override val screen: Screen = SettingsScreen.SECOND

    @Composable
    override fun Content(contentArgs: ContentArgs) {
        super.Content(contentArgs)
        
        // When a parent is injecting ViewModel with hiltViewModel, it has to called in child too,
        // so actionViewModel<BriefActionViewModel>(contentArgs) has to be called inside child Content,
        // otherwise the app crashes with "Compose Runtime internal error. Unexpected or incorrect use of
        // the Compose internal runtime API (Cannot seek outside the current group (106-1394))."
        actionViewModel<BriefActionViewModel>(contentArgs)

        Text("This is Settings Two")
    }
}
