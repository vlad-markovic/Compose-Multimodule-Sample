package com.vladmarkovic.sample.common.navigation.screen.navcomponent.model

import com.vladmarkovic.sample.common.mv.action.NavigationAction

typealias CloseDrawer = CommonNavigationAction.Back

/** Common [NavigationAction]s. */
sealed class CommonNavigationAction : NavigationAction {
    data object Back : CommonNavigationAction()
    data object OpenDrawer : CommonNavigationAction()
}