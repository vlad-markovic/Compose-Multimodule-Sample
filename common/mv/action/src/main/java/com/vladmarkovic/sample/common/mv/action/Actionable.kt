package com.vladmarkovic.sample.common.mv.action

interface Actionable<A: Action> {
    suspend fun emitAction(action: A)
}
