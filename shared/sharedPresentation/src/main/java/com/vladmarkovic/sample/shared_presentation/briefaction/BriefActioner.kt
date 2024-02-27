/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.briefaction

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/** Manages "sending" of [BriefAction]s. */
class BriefActioner {

    private val _action = MutableSharedFlow<BriefAction>()
    val action: SharedFlow<BriefAction> = _action.asSharedFlow()

    suspend fun action(action: BriefAction) {
        _action.emit(action)
    }
}
