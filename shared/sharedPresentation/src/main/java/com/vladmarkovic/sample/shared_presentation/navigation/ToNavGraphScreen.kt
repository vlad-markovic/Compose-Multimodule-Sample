/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation

import com.vladmarkovic.sample.common.view.action.NavigationAction
import com.vladmarkovic.sample.shared_domain.screen.NavGraphScreen

/** For typing and scoping screen navigation actions for navigating to [NavGraphScreen]. */
abstract class ToNavGraphScreen(
    val screen: NavGraphScreen,
    val args: Map<String, String>? = null
) : NavigationAction {
    constructor(screen: NavGraphScreen, arg: Pair<String, String>) : this(screen, mapOf(arg))
}
