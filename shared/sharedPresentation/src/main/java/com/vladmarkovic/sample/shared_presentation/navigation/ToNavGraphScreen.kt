/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation

import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction
import com.vladmarkovic.sample.shared_domain.screen.NavGraphScreen

/** For typing and scoping screen navigation actions for navigating to [NavGraphScreen]. */
abstract class ToNavGraphScreen(val screen: NavGraphScreen, val args: List<String>? = null) : NavigationAction {
    constructor(screen: NavGraphScreen, arg: String) : this(screen, listOf(arg))
    abstract val route: String
}
