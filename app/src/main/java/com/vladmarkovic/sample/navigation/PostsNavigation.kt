package com.vladmarkovic.sample.navigation

import androidx.fragment.app.FragmentManager
import com.vladmarkovic.sample.R
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.feed.FeedFragment
import com.vladmarkovic.sample.post_presentation.post.PostFragment
import com.vladmarkovic.sample.shared_domain.util.ignoreReturn

internal fun navigateToFeedScreen(fragmentManager: FragmentManager) {
    fragmentManager.beginTransaction()
        .replace(R.id.container, FeedFragment.newInstance())
        .commitNow()
}

internal fun navigateToPostScreen(fragmentManager: FragmentManager, post: Post) {
    fragmentManager.beginTransaction()
        .add(R.id.container, PostFragment.newInstance(post))
        .addToBackStack(null)
        .commit()
        .ignoreReturn()
}
