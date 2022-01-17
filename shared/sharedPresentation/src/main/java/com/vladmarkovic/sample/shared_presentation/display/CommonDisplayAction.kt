/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.display

import android.view.Display
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.DisplayAction
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes


/** Common [Display] actions. */
sealed class CommonDisplayAction : DisplayAction {
    data class Toast(val value: StrOrRes) : CommonDisplayAction()
    object OpenDrawer : CommonDisplayAction()
}
