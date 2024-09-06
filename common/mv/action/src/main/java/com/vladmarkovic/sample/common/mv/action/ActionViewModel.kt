/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.mv.action

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Base [ViewModel] which can send [Action]s. */
@HiltViewModel
open class ActionViewModel @Inject constructor() : ViewModel(),
    ActionMonitorable<Action>, Actionable<Action> {

    private val actioner: Actioner<Action> = Actioner()

    override val actions: SharedFlow<Action> get() = actioner.actions

    override suspend fun emitAction(action: Action) = actioner.emitAction(action)
}


fun <T> T.navigate(action: NavigationAction): Job where T : ViewModel, T : Actionable<Action> =
    viewModelScope.launch { emitAction(action) }

fun <T> T.display(action: DisplayAction): Job where T : ViewModel, T : Actionable<Action> =
    viewModelScope.launch { emitAction(action) }

fun <T> T.action(action: Action): Job where T : ViewModel, T : Actionable<Action> =
    viewModelScope.launch { emitAction(action) }
