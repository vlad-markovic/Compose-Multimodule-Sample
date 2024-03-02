package com.vladmarkovic.sample.shared_presentation.util

import androidx.navigation.NavController

fun NavController.isScreenVisible(route: String?): Boolean =
    route != null && currentDestination?.route?.contains(route) ?: true
