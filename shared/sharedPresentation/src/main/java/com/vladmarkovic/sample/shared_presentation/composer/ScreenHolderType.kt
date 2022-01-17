package com.vladmarkovic.sample.shared_presentation.composer

/**
 * Initial backstack size is
 *  2: when at first screen of a standalone screen holder (main and first screen)
 *  3: when at first screen of initial tab (main, first tab, first tab first screen),
 *  5: when at first screen of any other next tab (+tab, +tab first screen)
 */
enum class ScreenHolderType(val initialBackstackSize: Int) {
    STANDALONE(2), INITIAL_TAB(3), TAB(5)
}
