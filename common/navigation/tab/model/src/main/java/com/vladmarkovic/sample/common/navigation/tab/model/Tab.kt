package com.vladmarkovic.sample.common.navigation.tab.model

import com.vladmarkovic.sample.common.navigation.screen.model.Screen

/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

interface Tab {
    val name: String
    val ordinal: Int
    val screens: List<Screen>
    val initialScreen: Screen get() = screens.first()
}
