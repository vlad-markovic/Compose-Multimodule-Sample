/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.lifecycle.ViewModel
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionable

/** Defines Compose UI and elements for a Tab Screen. */
interface TabScreenComposer<VM> : ScreenComposer<VM> where VM : BriefActionable, VM : ViewModel {
    override val type: ScreenHolderType get() = ScreenHolderType.TAB
}

/** Defines Compose UI and elements for a Tab Initial Screen. */
interface TabInitialScreenComposer<VM> : ScreenComposer<VM> where VM : BriefActionable, VM : ViewModel {
    override val type: ScreenHolderType get() = ScreenHolderType.INITIAL_TAB
}
