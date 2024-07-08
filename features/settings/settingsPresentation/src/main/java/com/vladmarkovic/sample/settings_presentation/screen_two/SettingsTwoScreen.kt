/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.settings_presentation.screen_two

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.ScaffoldData
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.compose.di.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.str


@Composable
fun SettingsTwoScreen(bubbleUp: (BriefAction) -> Unit) {
    val viewModel = actionViewModel<BriefActionViewModel>(bubbleUp)
    bubbleUp(
        ScaffoldData(
            topBarTitle = "Settings Two".str,
            upButton = UpButton.BackButton(viewModel),
        )
    )
    SettingsTwoScreen()
}

@Composable
private fun SettingsTwoScreen() {
    Text("This is Settings Two")
}
