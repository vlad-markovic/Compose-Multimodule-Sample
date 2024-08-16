/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.settings_presentation.main

import com.vladmarkovic.sample.shared_domain.screen.SettingsScreen
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.ToNavGraphScreen

data object ToSecondSettingsScreen : ToNavGraphScreen(SettingsScreen.SECOND)
