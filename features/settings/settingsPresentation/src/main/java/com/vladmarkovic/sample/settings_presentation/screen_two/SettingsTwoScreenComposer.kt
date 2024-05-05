/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.settings_presentation.screen_two

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposer
import com.vladmarkovic.sample.shared_domain.screen.Screen
import com.vladmarkovic.sample.shared_domain.screen.SettingsScreen
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.str
import dagger.hilt.android.scopes.ViewModelScoped
import java.util.Optional
import javax.inject.Inject

/** Defines Compose UI and elements for second settings screen. */
@ViewModelScoped
class SettingsTwoScreenComposer @Inject constructor() : ScreenComposer<BriefActionViewModel>() {

    override val screen: Screen = SettingsScreen.SECOND

    @Composable
    override fun viewModel(bubbleUp: (BriefAction) -> Unit): BriefActionViewModel =
        actionViewModel<BriefActionViewModel>(bubbleUp)

    override fun topBarChange(viewModel: BriefActionViewModel): Optional<@Composable () -> Unit> =
        defaultTopBarChange("Settings Two".str, upButton = UpButton.BackButton(viewModel))

    @Composable
    override fun Content(viewModel: BriefActionViewModel) {
        Text("This is Settings Two")
    }
}
