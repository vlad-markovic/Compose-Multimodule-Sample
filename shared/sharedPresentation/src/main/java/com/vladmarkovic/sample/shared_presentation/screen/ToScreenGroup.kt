/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.screen

import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction

/**
 * Navigate to a new screen holder, like new activity which holds another group of screens.
 */
sealed interface ToScreenGroup: NavigationAction
