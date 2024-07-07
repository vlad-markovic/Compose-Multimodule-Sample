/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.briefaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Base [ViewModel] which can send [BriefAction]s. */
@HiltViewModel
open class BriefActionViewModel @Inject constructor() : ViewModel(), MutableSharedFlow<BriefAction> by MutableSharedFlow()

fun <T> T.navigate(action: BriefAction.NavigationAction) where T: ViewModel, T: MutableSharedFlow<BriefAction> = action(action)
fun <T> T.display(action: BriefAction.DisplayAction) where T: ViewModel, T: MutableSharedFlow<BriefAction> = action(action)
fun <T> T.action(action: BriefAction) where T: ViewModel, T: MutableSharedFlow<BriefAction> {
    viewModelScope.launch { emit(action) }
}
