/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionable
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import kotlinx.coroutines.flow.MutableStateFlow

/** ScreenComposer with hamburger as up button - opens the drawer on click. */
abstract class DrawerScreenComposer<VM> : ScreenComposer<VM>() where VM : BriefActionable, VM : ViewModel {

    override val upButton: MutableStateFlow<UpButton> = MutableStateFlow(UpButton.DrawerButton {})

    @Composable
    override fun Content(contentArgs: ContentArgs, viewModel: VM) {
        super.Content(contentArgs, viewModel)
        LaunchedEffect(contentArgs) { upButton.value = UpButton.DrawerButton(viewModel) }
    }
}
