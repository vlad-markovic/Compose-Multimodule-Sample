/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.viewaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Base [ViewModel] which can send [ViewAction]s. */
@HiltViewModel
open class ActionViewModel @Inject constructor() : ViewModel(), MutableSharedFlow<ViewAction> by MutableSharedFlow()

fun <T> T.navigate(action: ViewAction.NavigationAction) where T: ViewModel, T: MutableSharedFlow<ViewAction> = action(action)
fun <T> T.display(action: ViewAction.DisplayAction) where T: ViewModel, T: MutableSharedFlow<ViewAction> = action(action)
fun <T> T.action(action: ViewAction) where T: ViewModel, T: MutableSharedFlow<ViewAction> {
    viewModelScope.launch { emit(action) }
}
