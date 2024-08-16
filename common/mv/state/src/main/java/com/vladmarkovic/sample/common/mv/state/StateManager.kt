package com.vladmarkovic.sample.common.mv.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StateManager<out State, in Change>(
    initialState: State,
    private val stateReducer: StateReducer<State, Change>
) : StateChangeable<State, Change>, StateMonitorable<State> {

    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)

    override val state: StateFlow<State> = _state.asStateFlow()

    override fun changeState(change: Change) {
        _state.value = stateReducer(_state.value, change)
    }
}
