/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionable
import com.vladmarkovic.sample.shared_presentation.briefaction.action
import com.vladmarkovic.sample.shared_presentation.compose.DefaultTopBar
import com.vladmarkovic.sample.shared_presentation.compose.OnStart
import com.vladmarkovic.sample.shared_presentation.compose.ScaffoldChange
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.drawer.DefaultDrawer
import com.vladmarkovic.sample.shared_presentation.ui.drawer.DrawerItem
import com.vladmarkovic.sample.shared_presentation.ui.model.MenuItem
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import java.util.Optional

/** Defines Compose UI and elements for a Screen. */
abstract class ScreenComposer<VM> where VM : BriefActionable, VM : ViewModel {

    abstract val screen: Screen

    @Composable
    fun Content(args: ScreenArgs, holderType: ScreenHolderType) {
        val viewModel = viewModel(args)
        val scaffoldChange = remember {
            ScaffoldChange(screen, holderType, topBarChange(args, viewModel), drawerChange(args, viewModel))
        }
        OnStart { viewModel.action(scaffoldChange) }

        // BackHandler(viewModel) // TODO? If cannot, otherwise remove [viewModel] method, and let each inject VM.
        Content(args, viewModel)
    }


    /** Override to specify a [Composable] content for this screen. */
    @Composable
    protected abstract fun Content(args: ScreenArgs, viewModel: VM)

    @Composable
    abstract fun viewModel(args: ScreenArgs): VM

    abstract fun topBarChange(args: ScreenArgs, viewModel: VM): Optional<(@Composable () -> Unit)>?

    open fun drawerChange(args: ScreenArgs, viewModel: VM): Optional<(@Composable ColumnScope.() -> Unit)>? =
        Optional.empty() // no-drawer; null optional means no change

    protected fun defaultTopBarChange(
        title: StrOrRes?,
        upButton: UpButton? = null,
        menuItems: List<MenuItem>? = null
    ): Optional<(@Composable () -> Unit)> = Optional.of {
        DefaultTopBar(title = title, upButton = upButton, menuItems = menuItems)
    }

    protected fun defaultDrawerChange(drawerItems: List<DrawerItem>): Optional<(@Composable ColumnScope.() -> Unit)> =
        Optional.of { DefaultDrawer(drawerItems) }
}
