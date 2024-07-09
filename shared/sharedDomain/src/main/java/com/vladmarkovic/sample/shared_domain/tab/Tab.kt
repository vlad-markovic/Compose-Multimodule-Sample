/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_domain.tab

import com.vladmarkovic.sample.shared_domain.screen.NavGraphScreen

sealed interface Tab {
    val name: String
    val screens: List<NavGraphScreen>
    val initialScreen: NavGraphScreen get() = screens.first()
}
