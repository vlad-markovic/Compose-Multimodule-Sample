package com.vladmarkovic.sample.shared_presentation.screen

import androidx.navigation.NamedNavArgument
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.PostsScreen.ArgKeys.POST

sealed interface MainScreen : Screen {

    enum class PostsScreen : MainScreen {
        FEED_SCREEN,
        POST_SCREEN {
            override val args: List<NamedNavArgument> = listOf(stringArg(POST.name))
        };

        enum class ArgKeys {
            POST
        }
    }
}