/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.display

import android.view.Display
import androidx.annotation.StringRes
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.DisplayAction
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes


/** Common [Display] actions. */
sealed class CommonDisplayAction : DisplayAction {
    data class Toast(val value: StrOrRes) : CommonDisplayAction() {
        constructor(str: String): this(StrOrRes.str(str))
        constructor(@StringRes res: Int): this(StrOrRes.res(res))
    }
    data object OpenDrawer : CommonDisplayAction()
    data object CloseDrawer : CommonDisplayAction()
}
