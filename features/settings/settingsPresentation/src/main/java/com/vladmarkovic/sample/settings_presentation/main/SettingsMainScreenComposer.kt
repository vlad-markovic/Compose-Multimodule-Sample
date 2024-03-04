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
import com.vladmarkovic.sample.shared_presentation.compose.ScreenChange
import com.vladmarkovic.sample.shared_presentation.composer.ScreenArgs
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderType
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.screen.SettingsScreen
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.str
import javax.inject.Inject

/** Defines Compose UI and elements for main settings screen. */
class SettingsMainScreenComposer @Inject constructor() : ScreenComposer<BriefActionViewModel>() {

    override val screen: Screen = SettingsScreen.MAIN

    @Composable
    override fun viewModel(args: ScreenArgs): BriefActionViewModel =
        actionViewModel<BriefActionViewModel>(args.bubbleUp)

    override fun scaffoldChange(viewModel: BriefActionViewModel, holderType: ScreenHolderType): ScreenChange = change(
        holderType = holderType,
        topBarChange = topBarChange(
            title = "Settings".str,
            upButton = UpButton.BackButton(viewModel),
        )
    )

    @Composable
    override fun Content(args: ScreenArgs, viewModel: BriefActionViewModel) {
        Column (Modifier.padding(16.dp)) {
            Button({ viewModel.navigate(ToSecondSettingsScreen) }) {
                Text("Go To Settings Two")
            }
        }
    }
}
