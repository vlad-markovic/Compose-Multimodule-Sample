package com.vladmarkovic.sample.common.mv.action

import com.vladmarkovic.sample.core.coroutines.withMissedReplayed
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ViewActioner<ViewAction> : ViewActionMonitorable<ViewAction>, ViewActionable<ViewAction> {

    private val _actions: MutableSharedFlow<ViewAction> = MutableSharedFlow()

    override val actions: SharedFlow<ViewAction> get() = _actions.withMissedReplayed()

    override suspend fun emitAction(action: ViewAction) = _actions.emit(action)
}
