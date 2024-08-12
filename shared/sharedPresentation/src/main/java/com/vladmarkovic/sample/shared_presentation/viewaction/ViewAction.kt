/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.viewaction

/**
 * Represents non-persistent actions which should be "consumed" only once,
 * such as showing a short lived message or navigating to a screen.
 */
interface ViewAction {
    /** Actions moving away from a screen. */
    interface NavigationAction : ViewAction
    /** Actions happening while on a screen. */
    interface DisplayAction : ViewAction
}
