/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionable
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import kotlinx.coroutines.flow.MutableStateFlow

/** ScreenComposer with back arrow as up button - navigates back on click. */
abstract class BackScreenComposer<VM> : ScreenComposer<VM>() where VM : BriefActionable, VM : ViewModel {

    override val upButton: MutableStateFlow<UpButton> = MutableStateFlow(UpButton.BackButton {})

    @Composable
    override fun Content(contentArgs: ContentArgs, viewModel: VM) {
        super.Content(contentArgs, viewModel)
        LaunchedEffect(contentArgs) { upButton.value = UpButton.BackButton(viewModel) }
    }
}
