package com.vladmarkovic.sample.shared_presentation.navigation

import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction

/**
 * sealed version of [BriefAction.NavigationAction], scoping them for this app,
 * for exhaustive when statement when handling.
 */
sealed interface NavigationAction : BriefAction.NavigationAction
