package com.vladmarkovic.sample.shared_presentation.tab

import com.vladmarkovic.sample.common.navigation.tab.model.Tab

val Tab.route: String get() = "$name?${TabArgs.TAB_ORDINAL.name}=$ordinal"

enum class TabArgs {
    TAB_ORDINAL
}
