/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation

import android.app.Activity
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.ToScreenGroup

/** Abstracts, to decorate Application class, to be able to use it in a different module. */
interface TopNavigationActionHandler {
    fun handleTopScreenNavigationAction(activity: Activity, action: ToScreenGroup)
}
