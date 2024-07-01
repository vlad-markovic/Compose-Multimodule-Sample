package com.vladmarkovic.sample.shared_presentation.compose.navscaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import com.vladmarkovic.sample.shared_domain.screen.Screen
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultTopBar
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultDrawer
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppColor
import com.vladmarkovic.sample.shared_presentation.util.safeValue
import com.vladmarkovic.sample.shared_presentation.util.scaffoldDataManager

@Composable
fun DefaultScreensNavScaffold(
    allScreens: List<Screen>,
    initialScreen: Screen = allScreens.first(),
    actionHandler: @DisallowComposableCalls (BriefAction) -> Unit = remember {
        { throw IllegalStateException("Unhandled action: $it") }
    },
) {
    val scaffoldData: ScaffoldDataManager = scaffoldDataManager(initialScreen)
    val scaffoldChangesHandler: (BriefAction) -> Unit = remember {
        { action ->
            when (action) {
                is ScaffoldData -> scaffoldData.update(action)
                else -> actionHandler(action)
            }
        }
    }

    val drawerItems = scaffoldData.drawerItems.safeValue

    ScreensNavScaffold(
        allScreens = allScreens,
        initialScreen = initialScreen,
        topBar = remember {{
            DefaultTopBar(
                data = scaffoldData.topBarData.safeValue,
                backgroundColor = AppColor.Grey900
            )
        }},
        drawerContent = remember(drawerItems) {
            drawerItems?.let {{ DefaultDrawer(drawerItems = drawerItems) }}
        },
        actionHandler = scaffoldChangesHandler
    )
}
