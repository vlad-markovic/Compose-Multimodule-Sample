/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.navigation.screen.navcomponent.util

import androidx.navigation.NavController
import com.vladmarkovic.sample.core.android.asActivity


fun NavController.onBack(isDrawerOpen: () -> Boolean, closeDrawer: () -> Unit) {
    when {
        isDrawerOpen() -> closeDrawer()
        isFirstScreen -> context.asActivity.finish() // TODO circular if onBackPress is called; how to do for Fragments?
        else -> popBackStack()
    }
}
