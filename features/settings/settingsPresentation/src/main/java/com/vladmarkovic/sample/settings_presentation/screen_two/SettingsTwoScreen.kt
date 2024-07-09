/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.settings_presentation.screen_two

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.ScaffoldData
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.compose.di.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.str


@Composable
fun SettingsTwoScreen(
    bubbleUp: (BriefAction) -> Unit,
    viewModel: BriefActionViewModel = actionViewModel<BriefActionViewModel>(bubbleUp)
) {
    LaunchedEffect(Unit) {
        bubbleUp(
            ScaffoldData(
                topBarTitle = "Settings Two".str,
                upButton = UpButton.BackButton(viewModel),
                topBarTransitionDirection = -1,
            )
        )
    }
    SettingsTwoScreen()
}

@Composable
private fun SettingsTwoScreen(modifier: Modifier = Modifier) {
    Box(Modifier.fillMaxSize().then(modifier)) {
        Text("This is Settings Two")
    }
}
