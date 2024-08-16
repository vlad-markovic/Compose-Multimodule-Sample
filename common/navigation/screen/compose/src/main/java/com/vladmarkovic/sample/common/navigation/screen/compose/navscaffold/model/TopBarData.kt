package com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.model

import com.vladmarkovic.sample.common.android.model.StrOrRes

class TopBarData(
    val title: StrOrRes? = null,
    val upButton: UpButton? = null,
    val menuItems: List<MenuItem>? = null,
)

