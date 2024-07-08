package com.vladmarkovic.sample.shared_presentation.compose.navscaffold

import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DrawerItem
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.TopBarData
import com.vladmarkovic.sample.shared_presentation.ui.model.MenuItem
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton

class ScaffoldData(
    val topBar: TopBarData,
    val drawerItems: DrawerData,
): BriefAction.DisplayAction {
    constructor(
        topBarTitle: StrOrRes?,
        upButton: UpButton? = null,
        menuItems: List<MenuItem>? = null,
        drawerItems: List<DrawerItem>? = null
    ) : this(TopBarData(topBarTitle, upButton, menuItems), DrawerData(drawerItems))
}

class DrawerData(val drawerItems: List<DrawerItem>?)
