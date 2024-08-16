package com.vladmarkovic.sample.common.mv.action

interface ViewActionable<ViewAction> {
    suspend fun emitAction(action: ViewAction)
}
