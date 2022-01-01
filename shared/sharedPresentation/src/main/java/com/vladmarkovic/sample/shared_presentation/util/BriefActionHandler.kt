package com.vladmarkovic.sample.shared_presentation.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel

/** Handling of passed observed [BriefAction]s from [BriefActionViewModel]s from a Fragment. */
fun Fragment.handleBriefAction(action: BriefAction) =
    requireActivity().handleBriefAction(action)

/** Handling of passed observed [BriefAction]s from [BriefActionViewModel]s from a Activity. */
fun FragmentActivity.handleBriefAction(action: BriefAction) = when (action) {
    is NavigationAction -> withNavigation { navigate(action) }
    else -> throw IllegalArgumentException("Unhandled action: $action")
}
