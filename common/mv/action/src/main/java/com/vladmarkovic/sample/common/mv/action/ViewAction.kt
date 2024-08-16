/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.mv.action

/**
 * Represents non-persistent actions which should be "consumed" only once,
 * such as showing a short lived message or navigating to a screen.
 * Called a "View"Action because it is directed to (consumed by) the "view"
 */
interface ViewAction
