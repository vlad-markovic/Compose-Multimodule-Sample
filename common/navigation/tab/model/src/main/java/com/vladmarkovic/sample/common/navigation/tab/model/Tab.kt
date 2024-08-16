package com.vladmarkovic.sample.common.navigation.tab.model

import com.vladmarkovic.sample.common.navigation.screen.model.NavGraphScreen

/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

interface Tab {
    val name: String
    val ordinal: Int
    val screens: List<NavGraphScreen>
    val initialScreen: NavGraphScreen get() = screens.first()
}
