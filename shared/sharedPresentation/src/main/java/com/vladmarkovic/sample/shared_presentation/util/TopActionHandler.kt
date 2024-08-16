/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.vladmarkovic.sample.common.android.util.toast
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.ToScreenGroup
import com.vladmarkovic.sample.common.mv.action.ViewAction
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.ScaffoldDataManager
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.model.ScaffoldData
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.rememberScaffoldDataManager
import com.vladmarkovic.sample.common.navigation.screen.model.Screen
import com.vladmarkovic.sample.shared_presentation.display.CommonDisplayAction


@Composable
fun rememberTopActionHandler(
    initialScreen: Screen,
    bubbleUp: (ViewAction) -> Unit
): Pair<ScaffoldDataManager, (ViewAction) -> Unit> {
    val context = LocalContext.current
    val scaffoldDataManager: ScaffoldDataManager = rememberScaffoldDataManager(initialScreen)
    return scaffoldDataManager to remember {{ action ->
        when(action) {
            is CommonDisplayAction.Toast -> context.toast(action.value.get(context))
            is ToScreenGroup -> context.handleTopScreenNavigationAction(action)
            is ScaffoldData -> scaffoldDataManager.update(action)
            else -> bubbleUp(action)
        }
    }}
}
