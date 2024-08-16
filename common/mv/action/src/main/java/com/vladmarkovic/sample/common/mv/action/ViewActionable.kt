package com.vladmarkovic.sample.common.mv.action

interface ViewActionable<A: ViewAction> {
    suspend fun emitAction(action: A)
}
