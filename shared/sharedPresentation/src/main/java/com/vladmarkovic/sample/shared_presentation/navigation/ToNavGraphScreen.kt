/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation

import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction
import com.vladmarkovic.sample.shared_domain.screen.NavGraphScreen
import com.vladmarkovic.sample.shared_presentation.screen.routeArgs

/** For typing and scoping screen navigation actions for navigating to [NavGraphScreen]. */
abstract class ToNavGraphScreen(val screen: NavGraphScreen) : NavigationAction {

    private var _args: List<String>? = null
    val args: List<String>? get() = _args
    constructor(screen: NavGraphScreen, arg: String) : this(screen) {
        _args = _args?.let { it + arg } ?: listOf(arg)
    }
    constructor(screen: NavGraphScreen, args: List<String>) : this(screen) {
        _args = _args?.let { it + args } ?: args
    }
}

val ToNavGraphScreen.route: String get() = screen.name + screen.routeArgs(args)
