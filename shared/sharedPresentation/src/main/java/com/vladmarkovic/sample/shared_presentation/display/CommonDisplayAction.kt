/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.display

import android.view.Display
import androidx.annotation.StringRes
import com.vladmarkovic.sample.common.android.model.StrOrRes
import com.vladmarkovic.sample.common.view.action.DisplayAction


/** Common [Display] actions. */
sealed class CommonDisplayAction : DisplayAction {
    data class Toast(val value: StrOrRes) : CommonDisplayAction() {
        constructor(str: String): this(StrOrRes.str(str))
        constructor(@StringRes res: Int): this(StrOrRes.res(res))
    }
}
