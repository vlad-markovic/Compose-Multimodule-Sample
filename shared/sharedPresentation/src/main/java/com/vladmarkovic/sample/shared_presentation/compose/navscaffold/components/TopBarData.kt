package com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components

import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.ui.model.MenuItem
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton

data class TopBarData(
    val title: StrOrRes? = null,
    val upButton: UpButton? = null,
    val menuItems: List<MenuItem>? = null,
)
