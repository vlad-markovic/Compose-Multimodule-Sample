package com.vladmarkovic.sample.navigation

import androidx.fragment.app.FragmentActivity
import com.vladmarkovic.sample.R
import com.vladmarkovic.sample.feed_presentation.FeedFragment
import com.vladmarkovic.sample.shared_presentation.navigation.Navigation

/** Navigation implementation */
object AppNavigation : Navigation {

    override fun FragmentActivity.toFeed() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, FeedFragment.newInstance())
            .commitNow()
    }
}
