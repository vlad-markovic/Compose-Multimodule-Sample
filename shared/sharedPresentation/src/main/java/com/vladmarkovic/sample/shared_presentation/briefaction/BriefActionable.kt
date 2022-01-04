/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.briefaction

import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.*

/**
 * For decorating a ViewModel to give it functionality to send [DisplayAction]s and [NavigationAction]s,
 * for enabling composition - ViewModel just needs to instantiate the [BriefActioner];
 * or inherit from [BriefActionViewModel]
 */
interface BriefActionable {

    val actioner: BriefActioner

    val action: LiveAction<BriefAction> get() = actioner.action

    fun action(action: BriefAction) {
        actioner.action(action)
    }

    fun navigate(action: NavigationAction) {
        actioner.action(action)
    }

    fun display(action: DisplayAction) {
        actioner.action(action)
    }
}
