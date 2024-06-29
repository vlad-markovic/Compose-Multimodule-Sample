/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.settings_presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.briefaction.navigate
import com.vladmarkovic.sample.shared_presentation.compose.ScaffoldChange
import com.vladmarkovic.sample.shared_presentation.compose.defaultTopBarLambda
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.str

@Composable
fun SettingsMainScreen(
    updateScaffold: (ScaffoldChange) -> Unit,
    bubbleUp: (BriefAction) -> Unit
) {
    val viewModel = actionViewModel<BriefActionViewModel>(bubbleUp)
    updateScaffold(ScaffoldChange.TopBarChange.MaybeCompose(defaultTopBarLambda()))
    updateScaffold(ScaffoldChange.TopBarChange.Title("Settings".str))
    updateScaffold(ScaffoldChange.TopBarChange.ButtonUp(UpButton.BackButton(viewModel)))
    SettingsMainScreen { viewModel.navigate(ToSecondSettingsScreen) }
}

@Composable
private fun SettingsMainScreen(navToSecondScreen: () -> Unit) {
    Column (Modifier.padding(16.dp)) {
        Button(navToSecondScreen) {
            Text("Go To Settings Two")
        }
    }
}
