/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.screen

import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.ArgKeys.COUNTRY_INFO
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.ArgKeys.POST

sealed interface MainScreen : Screen {

    enum class PostsScreen : MainScreen {
        FEED_SCREEN,
        POST_SCREEN {
            override val argNames: List<String> = listOf(POST.name)
        }
    }

    enum class CovidScreen : MainScreen {
        COVID_COUNTRY_COMPARISON,
        COVID_COUNTRY_INFO {
            override val argNames: List<String> = listOf(COUNTRY_INFO.name)
        }
    }

    enum class ArgKeys {
        POST, COUNTRY_INFO
    }
}