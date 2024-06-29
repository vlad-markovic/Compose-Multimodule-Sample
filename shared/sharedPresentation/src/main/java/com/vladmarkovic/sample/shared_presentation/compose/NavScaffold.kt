package com.vladmarkovic.sample.shared_presentation.compose

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vladmarkovic.sample.shared_domain.screen.Screen
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.navigation.CommonNavigationAction
import com.vladmarkovic.sample.shared_presentation.navigation.ComposeScaffoldDataManager
import com.vladmarkovic.sample.shared_presentation.ui.drawer.DrawerItem
import com.vladmarkovic.sample.shared_presentation.ui.model.MenuItem
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme
import com.vladmarkovic.sample.shared_presentation.util.handleAction
import com.vladmarkovic.sample.shared_presentation.util.scaffoldDataManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow


sealed class ScaffoldChange {
    data class ScreenChange(val screen: Screen) : ScaffoldChange()

    sealed class TopBarChange : ScaffoldChange() {
        data class Title(val title: StrOrRes?) : TopBarChange()
        data class Modify(val modifier: Modifier) : TopBarChange()
        data class AlignText(val textAlign: TextAlign) : TopBarChange()
        data class ButtonUp(val upButton: UpButton?) : TopBarChange()
        data class MenuItems(val menuItems: List<MenuItem>?) : TopBarChange()
        data class Elevation(val elevation: Dp?) : TopBarChange()
        data class Background(val background: Color?) : TopBarChange()
        data class All(
            val title: StrOrRes?,
            val modifier: Modifier = Modifier,
            val textAlign: TextAlign = TextAlign.Start,
            val upButton: UpButton? = null,
            val menuItems: List<MenuItem>? = null,
            val elevation: Dp = AppBarDefaults.TopAppBarElevation,
            val background: Color = Color.Transparent,
            val maybeTopBar: @Composable (
                title: StateFlow<StrOrRes?>,
                modifier: StateFlow<Modifier>,
                textAlign: StateFlow<TextAlign>,
                upButton: StateFlow<UpButton?>,
                menuItems: StateFlow<List<MenuItem>?>,
                elevation: StateFlow<Dp?>,
                background: StateFlow<Color?>
            ) -> Unit
        ) : TopBarChange()
        data class Compose(
            val topBar: @Composable (
                title: StateFlow<StrOrRes?>,
                modifier: StateFlow<Modifier>,
                textAlign: StateFlow<TextAlign>,
                upButton: StateFlow<UpButton?>,
                menuItems: StateFlow<List<MenuItem>?>,
                elevation: StateFlow<Dp?>,
                background: StateFlow<Color?>
            ) -> Unit
        ) : TopBarChange()
        data class MaybeCompose(
            val topBar: @Composable (
                title: StateFlow<StrOrRes?>,
                modifier: StateFlow<Modifier>,
                textAlign: StateFlow<TextAlign>,
                upButton: StateFlow<UpButton?>,
                menuItems: StateFlow<List<MenuItem>?>,
                elevation: StateFlow<Dp?>,
                background: StateFlow<Color?>
            ) -> Unit
        ) : TopBarChange()
    }

    sealed class DrawerChange : ScaffoldChange() {
        data class DrawerItems(val drawerItems: List<DrawerItem>?) : DrawerChange()
        data class Modify(val modifier: Modifier) : DrawerChange()
        data class Background(val background: Color?) : DrawerChange()
        data class All(
            val drawerItems: List<DrawerItem>?,
            val modifier: Modifier,
            val background: Color?,
            val maybeDrawer: @Composable ColumnScope.(
                drawerItems: StateFlow<List<DrawerItem>?>,
                modifier: StateFlow<Modifier>,
                background: StateFlow<Color?>,
            ) -> Unit
        ) : DrawerChange()
        data class Compose(val drawer: @Composable ColumnScope.(
            drawerItems: StateFlow<List<DrawerItem>?>,
            modifier: StateFlow<Modifier>,
            background: StateFlow<Color?>,
        ) -> Unit) : DrawerChange()
        data class MaybeCompose(val drawer: @Composable ColumnScope.(
            drawerItems: StateFlow<List<DrawerItem>?>,
            modifier: StateFlow<Modifier>,
            background: StateFlow<Color?>,
        ) -> Unit) : DrawerChange()
    }
}


@Composable
fun NavScaffold(
    initialScreen: Screen,
    bottomBar: @Composable () -> Unit,
    bubbleUp: (BriefAction) -> Unit,
    navController: NavHostController = rememberNavController(),
    navHost: @Composable (
        modifier: Modifier,
        scaffoldChangeHandler: (ScaffoldChange) -> Unit,
        bubbleUp: (BriefAction) -> Unit
    ) -> Unit
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(Color.Black)

    val mainScope: CoroutineScope = rememberCoroutineScope()
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    val scaffoldData: ComposeScaffoldDataManager = scaffoldDataManager(initialScreen)

    val composeArgs = ComposeArgs(navController, mainScope, scaffoldState)

    AppTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = scaffoldData.topBar(),
            bottomBar = bottomBar,
            drawerContent = scaffoldData.drawer()
        ) { paddingValues ->
            navHost(
                Modifier.padding(paddingValues),
                remember { scaffoldData::update },
                remember {{ composeArgs.handleAction(it, bubbleUp) }}
            )

            composeArgs.BackHandler(bubbleUp)
        }
    }
}

@Composable
private fun ComposeArgs.BackHandler(actionHandler: (BriefAction) -> Unit) {
    androidx.activity.compose.BackHandler {
        handleAction(CommonNavigationAction.Back, actionHandler)
    }
}
