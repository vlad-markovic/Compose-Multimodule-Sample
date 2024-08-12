/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.view.action

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Base [ViewModel] which can send [ViewAction]s. */
@HiltViewModel
open class ActionViewModel @Inject constructor() : ViewModel(),
    ViewActionMonitorable<ViewAction>, ViewActionable<ViewAction> {

    private val actioner: ViewActioner<ViewAction> = ViewActioner()

    override val actions: SharedFlow<ViewAction> get() = actioner.actions

    override suspend fun emitAction(action: ViewAction) = actioner.emitAction(action)
}


fun <T> T.navigate(action: NavigationAction): Job where T : ViewModel, T : ViewActionable<ViewAction> =
    viewModelScope.launch { emitAction(action) }

fun <T> T.display(action: DisplayAction): Job where T : ViewModel, T : ViewActionable<ViewAction> =
    viewModelScope.launch { emitAction(action) }

fun <T> T.action(action: ViewAction): Job where T : ViewModel, T : ViewActionable<ViewAction> =
    viewModelScope.launch { emitAction(action) }
