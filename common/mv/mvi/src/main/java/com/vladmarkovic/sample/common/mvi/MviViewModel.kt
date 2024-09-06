package com.vladmarkovic.sample.common.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladmarkovic.sample.common.mv.action.DisplayAction
import com.vladmarkovic.sample.common.mv.action.NavigationAction
import com.vladmarkovic.sample.common.mv.action.ActionMonitorable
import com.vladmarkovic.sample.common.mv.action.Actioner
import com.vladmarkovic.sample.common.mv.state.StateManager
import com.vladmarkovic.sample.common.mv.state.StateMonitorable
import com.vladmarkovic.sample.common.mv.state.StateReducer
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class MviViewModel<out State, in Change, Action : com.vladmarkovic.sample.common.mv.action.Action, in Event>(
    initialState: State,
    stateReducer: StateReducer<State, Change>,
    private val stateManager: StateManager<State, Change> = StateManager(initialState, stateReducer),
    private val actioner: Actioner<com.vladmarkovic.sample.common.mv.action.Action> = Actioner()
) : ViewModel(),
    StateMonitorable<State> by stateManager,
    ActionMonitorable<com.vladmarkovic.sample.common.mv.action.Action> by actioner,
    ViewEventEmittable<Event> {

    protected fun changeState(change: Change) {
        stateManager.changeState(change)
    }

    protected fun action(action: Action): Job =
        viewModelScope.launch { actioner.emitAction(action) }

    protected fun navigate(action: NavigationAction): Job =
        viewModelScope.launch { actioner.emitAction(action) }

    protected fun display(action: DisplayAction): Job =
        viewModelScope.launch { actioner.emitAction(action) }
}
