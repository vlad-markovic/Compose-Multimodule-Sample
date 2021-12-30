package com.vladmarkovic.sample.shared_presentation.briefaction

/**
 * Represents non-persistent actions which should be "consumed" only once,
 * such as showing a short lived message or navigating to a screen.
 */
interface BriefAction {
    /** Actions moving away from a screen. */
    interface NavigationAction : BriefAction
    /** Actions happening while on a screen. */
    interface DisplayAction : BriefAction
}
