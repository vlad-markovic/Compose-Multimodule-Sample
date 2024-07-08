package com.vladmarkovic.sample.shared_presentation.compose.navscaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.vladmarkovic.sample.shared_domain.screen.Screen
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultDrawer
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultTopBar
import com.vladmarkovic.sample.shared_presentation.compose.safeValue
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppColor

@Composable
fun DefaultScreensNavScaffold(
    allScreens: List<Screen>,
    initialScreen: Screen = allScreens.first(),
    bubbleUp: (BriefAction) -> Unit = rememberThrowingNoHandler(),
) {
    val scaffoldData: ScaffoldDataManager = rememberScaffoldDataManager(initialScreen)
    val scaffoldChangesHandler: (BriefAction) -> Unit = rememberScaffoldChangesHandler(scaffoldData, bubbleUp)

    val drawerData = scaffoldData.drawerData.safeValue

    ScreensNavScaffold(
        allScreens = allScreens,
        initialScreen = initialScreen,
        topBar = remember {{
            DefaultTopBar(
                data = scaffoldData.topBarData.safeValue,
                backgroundColor = AppColor.Grey900
            )
        }},
        drawerContent = remember(drawerData) { drawerData?.drawerItems?.let {{ DefaultDrawer(it) }} },
        bubbleUp = scaffoldChangesHandler
    )
}
