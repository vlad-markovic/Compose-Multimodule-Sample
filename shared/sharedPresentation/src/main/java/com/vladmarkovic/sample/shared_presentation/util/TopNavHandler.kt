/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import android.content.Context
import com.vladmarkovic.sample.core.android.asActivity
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.ToScreenGroup
import com.vladmarkovic.sample.shared_presentation.navigation.TopNavigationActionHandler


val Context.asTopNavHandler: TopNavigationActionHandler get() =
    applicationContext as TopNavigationActionHandler

fun Context.handleTopScreenNavigationAction(action: ToScreenGroup) {
    asTopNavHandler.handleTopScreenNavigationAction(asActivity, action)
}
