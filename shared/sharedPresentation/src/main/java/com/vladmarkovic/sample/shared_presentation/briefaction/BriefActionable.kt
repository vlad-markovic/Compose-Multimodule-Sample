/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.briefaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.DisplayAction
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * For decorating a ViewModel to give it functionality to send [DisplayAction]s and [NavigationAction]s,
 * for enabling composition - ViewModel just needs to instantiate the [BriefActioner];
 * or inherit from [BriefActionViewModel]
 */
interface BriefActionable {
    val action: Flow<BriefAction>
    suspend fun asyncAction(action: BriefAction)
}

fun <T> T.navigate(action: NavigationAction) where T: ViewModel, T: BriefActionable = action(action)
fun <T> T.display(action: DisplayAction) where T: ViewModel, T: BriefActionable = action(action)
fun <T> T.action(action: BriefAction) where T: ViewModel, T: BriefActionable {
    viewModelScope.launch { asyncAction(action) }
}