/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

/**
 * Either an Activity (STANDALONE or INITIAL_TAB) or a ScreenHolderComposer (switcher) implementation class (TAB)
 * Initial backstack size is
 *  2: when at first screen of a standalone screen holder (main and first screen)
 *  3: when at first screen of initial tab (main, first tab, first tab first screen),
 *  5: when at first screen of any other next tab (+tab, +tab first screen)
 */
enum class ScreenHolderType(val initialBackstackSize: Int) {
    STANDALONE(2), INITIAL_TAB(3), TAB(5)
}
