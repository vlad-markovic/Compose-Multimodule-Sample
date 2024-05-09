/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_domain.tab

import com.vladmarkovic.sample.shared_domain.screen.Screen

sealed interface Tab {
    val name: String
    val screens: List<Screen>
    val initialScreen: Screen get() = screens.first()
}
