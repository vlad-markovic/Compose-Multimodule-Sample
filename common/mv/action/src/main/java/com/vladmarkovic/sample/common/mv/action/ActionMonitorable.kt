package com.vladmarkovic.sample.common.mv.action

import kotlinx.coroutines.flow.SharedFlow

interface ActionMonitorable<A: Action> {
    val actions: SharedFlow<Action>
}
