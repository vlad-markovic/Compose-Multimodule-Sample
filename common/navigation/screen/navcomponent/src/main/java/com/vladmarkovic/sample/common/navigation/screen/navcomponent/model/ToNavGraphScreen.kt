/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.navigation.screen.navcomponent.model

import com.vladmarkovic.sample.common.navigation.screen.model.NavGraphScreen
import com.vladmarkovic.sample.common.mv.action.NavigationAction

/** For typing and scoping screen navigation actions for navigating to [NavGraphScreen]. */
abstract class ToNavGraphScreen(
    val screen: NavGraphScreen,
    val args: Map<String, String>? = null
) : NavigationAction {
    constructor(screen: NavGraphScreen, arg: Pair<String, String>) : this(screen, mapOf(arg))
}
