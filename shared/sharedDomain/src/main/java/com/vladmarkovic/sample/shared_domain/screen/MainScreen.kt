package com.vladmarkovic.sample.shared_domain.screen

import com.vladmarkovic.sample.common.navigation.screen.model.Screen

sealed interface MainScreen : Screen {

    enum class PostsScreen : MainScreen {
        FEED_SCREEN,
        POST_SCREEN
    }

    enum class CovidScreen : MainScreen {
        COVID_COUNTRY_COMPARISON,
        COVID_COUNTRY_INFO
    }
}
