package com.vladmarkovic.sample.shared_domain.tab

import com.vladmarkovic.sample.shared_domain.screen.MainScreen

enum class MainBottomTab(override val screens: List<MainScreen>) : Tab {
    POSTS_TAB(MainScreen.PostsScreen.entries),
    COVID_TAB(MainScreen.CovidScreen.entries)
}
