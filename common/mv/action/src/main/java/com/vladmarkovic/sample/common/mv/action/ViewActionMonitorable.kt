package com.vladmarkovic.sample.common.mv.action

import kotlinx.coroutines.flow.SharedFlow

interface ViewActionMonitorable<A: ViewAction> {
    val actions: SharedFlow<ViewAction>
}
