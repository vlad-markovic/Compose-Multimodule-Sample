package com.vladmarkovic.sample.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.vladmarkovic.sample.R
import com.vladmarkovic.sample.main_presentation.ToFeedScreen
import com.vladmarkovic.sample.post_presentation.feed.FeedFragment
import com.vladmarkovic.sample.post_presentation.feed.ToPostScreen
import com.vladmarkovic.sample.post_presentation.post.PostFragment
import com.vladmarkovic.sample.shared_domain.util.ignoreReturn
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction
import com.vladmarkovic.sample.shared_presentation.navigation.Navigation

/**
 * Navigation implementation
 *  - To navigate using parentFragmentManager from a Fragment,
 *    either use requireActivity().navigateTo() or requireParentFragment.navigateTo(),
 *    depending if its parent is an Activity or a Fragment
 */
object AppNavigation : Navigation {

    override fun FragmentActivity.navigate(action: NavigationAction) =
        navigateTo(supportFragmentManager, action)

    override fun Fragment.navigate(action: NavigationAction) =
        navigateTo(childFragmentManager, action)

    private fun navigateTo(
        fragmentManager: FragmentManager,
        action: NavigationAction
    ) = when (action) {
        is ToFeedScreen ->  {
            fragmentManager.beginTransaction()
                .replace(R.id.container, FeedFragment.newInstance())
                .commitNow()
        }
        is ToPostScreen ->  {

            fragmentManager.beginTransaction()
                .add(R.id.container, PostFragment.newInstance(action.post))
                .addToBackStack(null)
                .commit()
                .ignoreReturn()
        }
        else -> throw IllegalArgumentException("Unhandled navigation action: $action")
    }
}
