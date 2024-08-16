package com.vladmarkovic.sample.common.mv.state

import kotlinx.coroutines.flow.StateFlow

interface StateMonitorable<out State> {
    val state: StateFlow<State>
}
