package com.vladmarkovic.sample.shared_presentation.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction

interface Navigation {

    fun FragmentActivity.navigate(action: NavigationAction)

    fun Fragment.navigate(action: NavigationAction)
}
