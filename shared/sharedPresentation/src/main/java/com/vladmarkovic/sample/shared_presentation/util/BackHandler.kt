/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import androidx.navigation.NavController
import com.vladmarkovic.sample.core.android.asActivity


fun NavController.onBack(isDrawerOpen: () -> Boolean, closeDrawer: () -> Unit) {
    when {
        isDrawerOpen() -> closeDrawer()
        isStackFirstScreen -> context.asActivity.finish() // TODO circular if onBackPress is called; how to do for Fragments?
        else -> popBackStack()
    }
}
