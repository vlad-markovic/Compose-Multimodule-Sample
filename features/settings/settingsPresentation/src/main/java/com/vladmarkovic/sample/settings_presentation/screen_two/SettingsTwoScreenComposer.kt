/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.settings_presentation.screen_two

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenArgs
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.screen.SettingsScreen
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.str
import javax.inject.Inject

/** Defines Compose UI and elements for second settings screen. */
class SettingsTwoScreenComposer @Inject constructor() : ScreenComposer<BriefActionViewModel>() {

    override val screen: Screen = SettingsScreen.SECOND

    @Composable
    override fun viewModel(args: ScreenArgs): BriefActionViewModel =
        actionViewModel<BriefActionViewModel>(args.bubbleUp)

    @Composable
    override fun Content(args: ScreenArgs, viewModel: BriefActionViewModel) {
        super.Content(args, viewModel)

        SetupScreen(
            args.screenSetup,
            change(
                topBarChange = topBarChange(
                    title = "Settings Two".str,
                    upButton = UpButton.BackButton(viewModel),
                )
            )
        )

        Text("This is Settings Two")
    }
}
