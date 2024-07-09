/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.settings_presentation.main

import com.vladmarkovic.sample.shared_domain.screen.SettingsScreen
import com.vladmarkovic.sample.shared_presentation.navigation.ToNavGraphScreen
import com.vladmarkovic.sample.shared_presentation.screen.navRoute

data object ToSecondSettingsScreen : ToNavGraphScreen(SettingsScreen.SECOND) {
    override val route: String = navRoute
}
