package com.vladmarkovic.sample.common.navigation.tab.navcomponent.util

import android.net.Uri
import androidx.navigation.NavBackStackEntry
import com.vladmarkovic.sample.common.navigation.tab.model.Tab
import com.vladmarkovic.sample.common.navigation.tab.navcomponent.model.TabArgs

val Tab<*>.route: String get() = "$name?${TabArgs.TAB_ORDINAL.name}=$ordinal"

val NavBackStackEntry.tabOrdinal: Int
    get() = Uri.parse(destination.parent?.route)
        .getQueryParameter(TabArgs.TAB_ORDINAL.name)
        ?.toIntOrNull()
        ?: 0
