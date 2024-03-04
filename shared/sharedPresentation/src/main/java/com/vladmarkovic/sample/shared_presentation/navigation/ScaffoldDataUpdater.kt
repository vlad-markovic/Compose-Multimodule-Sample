package com.vladmarkovic.sample.shared_presentation.navigation

import com.vladmarkovic.sample.shared_presentation.compose.ScreenChange

/** Holds and updates the state for the current screen. */
interface ScaffoldDataUpdater {
    fun update(change: ScreenChange)
}
