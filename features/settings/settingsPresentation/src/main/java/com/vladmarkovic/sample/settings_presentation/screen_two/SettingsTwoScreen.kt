/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.settings_presentation.screen_two

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.vladmarkovic.sample.common.mv.action.compose.actionViewModel
import com.vladmarkovic.sample.common.mv.action.ActionViewModel
import com.vladmarkovic.sample.common.mv.action.Action
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.model.ScaffoldData
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.model.UpButton
import com.vladmarkovic.sample.common.android.model.str


@Composable
fun SettingsTwoScreen(
    bubbleUp: (Action) -> Unit,
    viewModel: ActionViewModel = actionViewModel<ActionViewModel>(bubbleUp)
) {
    LaunchedEffect(Unit) {
        bubbleUp(
            ScaffoldData(
                topBarTitle = "Settings Two".str,
                upButton = UpButton.BackButton(viewModel),
            )
        )
    }
    SettingsTwoScreen()
}

@Composable
private fun SettingsTwoScreen(modifier: Modifier = Modifier) {
    Box(
        Modifier
            .fillMaxSize()
            .then(modifier)) {
        Text("This is Settings Two")
    }
}
