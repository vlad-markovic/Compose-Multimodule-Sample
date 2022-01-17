/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.navigation.CommonNavigationAction.Back
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel

/**
 * ScreenComposer with back arrow as up button - navigates back on click.
 * NOTE: when a parent is injecting ViewModel with hiltViewModel, it has to called in child too,
 * so actionViewModel<BriefActionViewModel>(contentArgs) has to be called inside child Content,
 * otherwise the app crashes with "Compose Runtime internal error. Unexpected or incorrect use of
 * the Compose internal runtime API (Cannot seek outside the current group (106-1394))."
 */
abstract class BackScreenComposer : ScreenComposer() {

    override val upButton: MutableState<UpButton> = mutableStateOf(UpButton.BackButton {})

    @Composable
    override fun Content(contentArgs: ContentArgs) {
        upButton.value = UpButton.BackButton(actionViewModel<BriefActionViewModel>(contentArgs))
    }
}
