package com.vladmarkovic.sample.navigation

import androidx.fragment.app.FragmentActivity
import com.vladmarkovic.sample.R
import com.vladmarkovic.sample.post_presentation.feed.FeedFragment
import com.vladmarkovic.sample.shared_presentation.navigation.HomeScreen
import com.vladmarkovic.sample.shared_presentation.navigation.Navigation
import com.vladmarkovic.sample.shared_presentation.navigation.Screen

/** Navigation implementation */
object AppNavigation : Navigation {

    override fun FragmentActivity.toScreen(screen: Screen) = when (screen) {
        HomeScreen.FEED ->  {
             supportFragmentManager.beginTransaction()
                 .replace(R.id.container, FeedFragment.newInstance())
                 .commitNow()
        }
    }
}
