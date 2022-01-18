/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.nav

import android.app.Activity
import android.content.Intent
import com.vladmarkovic.sample.settings_presentation.SettingsActivity
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction
import com.vladmarkovic.sample.shared_presentation.navigation.TopNavigationActionHandler
import com.vladmarkovic.sample.shared_presentation.screen.SettingsScreen
import com.vladmarkovic.sample.shared_presentation.screen.ToScreenGroup
import javax.inject.Inject
import javax.inject.Singleton

/** "Top" level [NavigationAction]s handler for handling [ToScreenGroup] [NavigationAction]s. */
@Singleton
class TopNavHandler @Inject constructor() : TopNavigationActionHandler {

    override fun handleTopScreenNavigationAction(activity: Activity, action: ToScreenGroup) =
        when (action) {
            is SettingsScreen.ToSettings -> activity.startActivity<SettingsActivity>()
        }

    private inline fun <reified T> Activity.startActivity() {
        startActivity(Intent(this, T::class.java))
    }
}
