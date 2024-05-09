/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation

import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction
import com.vladmarkovic.sample.shared_domain.screen.Screen
import com.vladmarkovic.sample.shared_presentation.screen.routeArgs

/** For typing and scoping screen navigation actions for navigating to [Screen]. */
abstract class ToScreen(val screen: Screen, val jsonArgs: List<String>? = null) : NavigationAction

val ToScreen.route: String get() = screen.name + screen.routeArgs(jsonArgs)
