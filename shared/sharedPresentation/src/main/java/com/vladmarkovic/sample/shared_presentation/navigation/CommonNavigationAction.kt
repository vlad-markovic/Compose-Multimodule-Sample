/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation

import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction

/** Common [NavigationAction]s. */
sealed class CommonNavigationAction : NavigationAction {
    object Back : CommonNavigationAction()
}
