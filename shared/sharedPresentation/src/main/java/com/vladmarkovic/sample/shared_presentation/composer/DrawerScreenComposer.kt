/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import kotlinx.coroutines.flow.MutableStateFlow

/** ScreenComposer with hamburger as up button - opens the drawer on click. */
abstract class DrawerScreenComposer : ScreenComposer() {

    override val upButton: MutableStateFlow<UpButton> = MutableStateFlow(UpButton.DrawerButton {})

    @Composable
    override fun Content(contentArgs: ContentArgs) {
        val actionViewModel = actionViewModel<BriefActionViewModel>(contentArgs)
        LaunchedEffect(contentArgs) { upButton.value = UpButton.DrawerButton(actionViewModel) }
    }
}
