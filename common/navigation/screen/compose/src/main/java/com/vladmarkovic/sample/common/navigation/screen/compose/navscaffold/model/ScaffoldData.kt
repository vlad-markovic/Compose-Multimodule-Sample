package com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.model

import com.vladmarkovic.sample.common.android.model.StrOrRes
import com.vladmarkovic.sample.common.mv.action.DisplayAction

class ScaffoldData(
    val topBar: TopBarData?,
    val drawerItems: DrawerData?,
): DisplayAction {
    constructor(
        topBarTitle: StrOrRes? = null,
        upButton: UpButton? = null,
        menuItems: List<MenuItem>? = null,
        drawerItems: List<DrawerItem>? = null,
    ) : this(TopBarData(topBarTitle, upButton, menuItems), DrawerData(drawerItems))

    constructor(drawerItems: List<DrawerItem>) : this(null, DrawerData(drawerItems))
}

class DrawerData(val drawerItems: List<DrawerItem>?)
