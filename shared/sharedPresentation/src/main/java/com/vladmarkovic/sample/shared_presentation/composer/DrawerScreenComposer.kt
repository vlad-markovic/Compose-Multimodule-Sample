/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel

/** ScreenComposer with hamburger as up button - opens the drawer on click. */
abstract class DrawerScreenComposer : ScreenComposer() {

    override val upButton: MutableState<UpButton> = mutableStateOf(UpButton.DrawerButton {})

    @Composable
    override fun Content(contentArgs: ContentArgs) {
        val actionViewModel = actionViewModel<BriefActionViewModel>(contentArgs)

        upButton.value = UpButton.DrawerButton(actionViewModel)
    }
}
