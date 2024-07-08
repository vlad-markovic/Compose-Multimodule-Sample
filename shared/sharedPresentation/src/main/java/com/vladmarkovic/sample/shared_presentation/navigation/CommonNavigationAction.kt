/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation

import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction

typealias CloseDrawer = CommonNavigationAction.Back

/** Common [NavigationAction]s. */
sealed class CommonNavigationAction : NavigationAction {
    data object Back : CommonNavigationAction()
    data object OpenDrawer : CommonNavigationAction()
}
