package com.vladmarkovic.sample.shared_presentation.briefaction

/** Manages "sending" of [BriefAction]s. */
class BriefActioner {

    private val _action = MutableLiveAction<BriefAction>()
    val action: LiveAction<BriefAction> = _action

    fun action(action: BriefAction) {
        _action.value = action
    }
}
