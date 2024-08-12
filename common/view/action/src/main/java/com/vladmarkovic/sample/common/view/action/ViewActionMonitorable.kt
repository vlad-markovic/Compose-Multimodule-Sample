package com.vladmarkovic.sample.common.view.action

import kotlinx.coroutines.flow.SharedFlow

interface ViewActionMonitorable<ViewAction> {
    val actions: SharedFlow<ViewAction>
}
