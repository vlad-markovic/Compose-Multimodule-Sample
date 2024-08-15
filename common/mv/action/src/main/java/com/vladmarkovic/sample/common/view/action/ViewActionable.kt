package com.vladmarkovic.sample.common.view.action

interface ViewActionable<ViewAction> {
    suspend fun emitAction(action: ViewAction)
}
