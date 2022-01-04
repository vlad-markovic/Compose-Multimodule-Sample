/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.navigation

import androidx.fragment.app.FragmentManager

internal fun navigateBack(fragmentManager: FragmentManager) {
    fragmentManager.popBackStack()
}
