package com.vladmarkovic.sample.shared_domain.screen

sealed interface Screen {
    val name: String
}

sealed interface NavGraphScreen : Screen
