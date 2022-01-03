/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.navigation.CommonNavigationAction
import com.vladmarkovic.sample.shared_presentation.navigation.ToScreen
import com.vladmarkovic.sample.shared_presentation.navigation.route

// region Fragment / Activity
/** Handling of passed observed [BriefAction]s from [BriefActionViewModel]s from a Fragment. */
fun Fragment.handleBriefAction(action: BriefAction) {
    view?.findNavController()?.handleBriefAction(action)
        ?: throw IllegalStateException("Cannot handle $action from Fragment while view is null.")
}

/** Handling of passed observed [BriefAction]s from [BriefActionViewModel]s from a Activity. */
fun FragmentActivity.handleBriefAction(action: BriefAction) {
    currentFocus?.findNavController()
        ?: throw IllegalStateException("Cannot handle $action from Activity while view is null.")

}
// endregion Fragment / Activity

// region Compose / NavController
fun NavController.handleBriefAction(action: BriefAction) {
    return when (action) {
        is NavigationAction -> navigate(action)
        else -> throw IllegalArgumentException("Unhandled action: $action")
    }
}

/** Branch out handling of different types of [NavigationAction]s. */
private fun NavController.navigate(action: NavigationAction) =
    when(action) {
        is ToScreen -> navigate(action.route)
        is CommonNavigationAction -> navigate(action)
        else -> throw IllegalArgumentException("Unhandled navigation action: $action")
    }

private fun NavController.navigate(action: CommonNavigationAction) =
    when (action) {
        is CommonNavigationAction.Back -> onBack()
    }

fun NavController.onBack() {
    if (isFirstScreen) (context as Activity).finish()
    else popBackStack()
}

val NavController.isFirstScreen: Boolean get() = currentBackStackEntry?.isFirstScreen == true
        && backQueue.size <= 2 // initial backstack size

private val NavBackStackEntry.isFirstScreen: Boolean get() =
    destination.parent?.startDestinationRoute == destination.route
// endregion Compose / NavController
