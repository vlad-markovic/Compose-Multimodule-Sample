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
import com.vladmarkovic.sample.common.mv.action.compose.actionViewModel
import com.vladmarkovic.sample.common.mv.action.ActionViewModel
import com.vladmarkovic.sample.common.mv.action.Action
import com.vladmarkovic.sample.common.mv.action.navigate
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.model.ScaffoldData
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.model.UpButton
import com.vladmarkovic.sample.common.android.model.str

@Composable
fun SettingsMainScreen(
    bubbleUp: (Action) -> Unit,
    viewModel: ActionViewModel = actionViewModel<ActionViewModel>(bubbleUp)
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
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)) {
        Button(navigateToSettingsTwo) {
            Text("Go To Settings Two")
        }
    }
}
