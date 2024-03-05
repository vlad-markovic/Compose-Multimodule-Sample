package com.vladmarkovic.sample.shared_presentation.navigation

import com.vladmarkovic.sample.shared_presentation.compose.ScaffoldChange

/** Holds and updates the state for the current screen. */
interface ScaffoldDataUpdater {
    fun update(change: ScaffoldChange)
}
