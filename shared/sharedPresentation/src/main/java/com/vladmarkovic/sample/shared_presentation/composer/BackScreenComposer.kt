/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * ScreenComposer with back arrow as up button - navigates back on click.
 * NOTE: when a parent is injecting ViewModel with hiltViewModel, it has to called in child too,
 * so actionViewModel<BriefActionViewModel>(contentArgs) has to be called inside child Content,
 * otherwise the app crashes with "Compose Runtime internal error. Unexpected or incorrect use of
 * the Compose internal runtime API (Cannot seek outside the current group (106-1394))."
 */
abstract class BackScreenComposer : ScreenComposer() {

    override val upButton: MutableStateFlow<UpButton> = MutableStateFlow(UpButton.BackButton {})

    @Composable
    override fun Content(contentArgs: ContentArgs) {
        val viewModel = actionViewModel<BriefActionViewModel>(contentArgs)
        LaunchedEffect(contentArgs) { upButton.value = UpButton.BackButton(viewModel) }
    }
}
