/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.settings_presentation.screen_two

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.compose.ScaffoldChange
import com.vladmarkovic.sample.shared_presentation.compose.defaultTopBarLambda
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.str


@Composable
fun SettingsTwoScreen(
    updateScaffold: (ScaffoldChange) -> Unit,
    bubbleUp: (BriefAction) -> Unit
) {
    val viewModel = actionViewModel<BriefActionViewModel>(bubbleUp)
    updateScaffold(ScaffoldChange.TopBarChange.MaybeCompose(defaultTopBarLambda()))
    updateScaffold(ScaffoldChange.TopBarChange.Title("Settings Two".str))
    updateScaffold(ScaffoldChange.TopBarChange.ButtonUp(UpButton.BackButton(viewModel)))
    SettingsTwoScreen()
}

@Composable
private fun SettingsTwoScreen() {
    Text("This is Settings Two")
}
