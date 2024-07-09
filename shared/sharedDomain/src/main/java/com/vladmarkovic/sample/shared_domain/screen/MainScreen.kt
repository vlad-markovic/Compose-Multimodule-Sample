package com.vladmarkovic.sample.shared_domain.screen

sealed interface MainScreen : NavGraphScreen {

    enum class PostsScreen : MainScreen {
        FEED_SCREEN,
        POST_SCREEN
    }

    enum class CovidScreen : MainScreen {
        COVID_COUNTRY_COMPARISON,
        COVID_COUNTRY_INFO
    }
}
