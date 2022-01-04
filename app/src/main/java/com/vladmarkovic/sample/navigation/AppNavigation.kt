/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.vladmarkovic.sample.main_presentation.ToFeedScreen
import com.vladmarkovic.sample.post_presentation.feed.ToPostScreen
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction
import com.vladmarkovic.sample.shared_presentation.navigation.CommonNavigationAction
import com.vladmarkovic.sample.shared_presentation.navigation.CommonNavigationAction.*
import com.vladmarkovic.sample.shared_presentation.navigation.Navigation
import com.vladmarkovic.sample.shared_presentation.navigation.ToScreen

/**
 * Navigation implementation for handling [NavigationAction]s
 *  - To navigate using parentFragmentManager from a Fragment,
 *    either use requireActivity().navigateTo() or requireParentFragment.navigateTo(),
 *    depending if its parent is an Activity or a Fragment
 */
object AppNavigation : Navigation {

    override fun FragmentActivity.navigate(action: NavigationAction) =
        navigate(supportFragmentManager, action)

    override fun Fragment.navigate(action: NavigationAction) =
        navigate(childFragmentManager, action)

    private fun navigate(fragmentManager: FragmentManager, action: NavigationAction) =
        when (action) {
            is ToScreen -> navigate(fragmentManager, action)
            is CommonNavigationAction -> navigate(fragmentManager, action)
            // If all actions were objects or enums, we could declare all in sharedPresentation
            // under a sealed interface, and then have a compile time type safety check
            // with an exhaustive when statement. But because some carry arguments which are
            // defined within a containing package, we need to fallback to a runtime check;
            // unless we'd define all the domain models in the sharedDomain too - not perfect either
            else -> throw IllegalArgumentException("Unhandled navigation action: $action")
        }

    private fun navigate(fragmentManager: FragmentManager, action: ToScreen) =
        when (action) {
            is ToFeedScreen -> navigateToFeedScreen(fragmentManager)
            is ToPostScreen -> navigateToPostScreen(fragmentManager, action.post)
            else -> throw IllegalArgumentException("Unhandled screen navigation action: $action")
        }

    private fun navigate(fragmentManager: FragmentManager, action: CommonNavigationAction) =
        when (action) {
            is Back -> navigateBack(fragmentManager)
        }
}
