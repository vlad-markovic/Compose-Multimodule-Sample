package com.vladmarkovic.sample.common.mv.action

import com.vladmarkovic.sample.core.coroutines.withMissedReplayed
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class Actioner<A: Action> : ActionMonitorable<A>, Actionable<A> {

    private val _actions: MutableSharedFlow<A> = MutableSharedFlow()

    override val actions: SharedFlow<A> get() = _actions.withMissedReplayed()

    override suspend fun emitAction(action: A) = _actions.emit(action)
}
