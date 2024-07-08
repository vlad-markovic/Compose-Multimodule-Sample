/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.settings_presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.briefaction.navigate
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.ScaffoldData
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.compose.di.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.str

@Composable
fun SettingsMainScreen(
    bubbleUp: (BriefAction) -> Unit,
    viewModel: BriefActionViewModel = actionViewModel<BriefActionViewModel>(bubbleUp)
) {
    LaunchedEffect(Unit) {
        bubbleUp(
            ScaffoldData(
                topBarTitle = "Settings".str,
                upButton = UpButton.BackButton(viewModel),
            )
        )
    }
    SettingsMainScreen { viewModel.navigate(ToSecondSettingsScreen) }
}

@Composable
private fun SettingsMainScreen(navigateToSettingsTwo: () -> Unit) {
    Column (Modifier.fillMaxSize().padding(16.dp)) {
        Button(navigateToSettingsTwo) {
            Text("Go To Settings Two")
        }
    }
}
