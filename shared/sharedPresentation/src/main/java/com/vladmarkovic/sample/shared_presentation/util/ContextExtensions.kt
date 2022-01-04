/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import android.content.Context
import com.vladmarkovic.sample.shared_presentation.navigation.NavigationProvider

val Context.navigation get() = (applicationContext as NavigationProvider).navigation

