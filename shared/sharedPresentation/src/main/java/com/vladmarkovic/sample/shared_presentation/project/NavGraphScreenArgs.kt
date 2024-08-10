/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.project

import com.vladmarkovic.sample.shared_domain.screen.MainScreen
import com.vladmarkovic.sample.shared_domain.screen.NavGraphScreen
import com.vladmarkovic.sample.shared_domain.tab.MainBottomTab
import com.vladmarkovic.sample.shared_presentation.screen.ScreenArgNames


val NavGraphScreen.extraArgsNames: Set<String> get() = when(this) {
    MainScreen.PostsScreen.POST_SCREEN -> setOf(ScreenArgNames.POST.name)
    MainScreen.CovidScreen.COVID_COUNTRY_INFO -> setOf(ScreenArgNames.COUNTRY_INFO.name)
    else -> emptySet()
}

val NavGraphScreen.stackOrdinal: Int? get() = when (this) {
    is MainScreen.PostsScreen -> MainBottomTab.POSTS_TAB.ordinal
    is MainScreen.CovidScreen -> MainBottomTab.COVID_TAB.ordinal
    else -> null
}
