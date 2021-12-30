package com.vladmarkovic.sample.shared_presentation.navigation

/** Defines a screen as navigation action, defining a [name] to represent it. */
sealed interface Screen : NavigationAction {
    val name: String
}
