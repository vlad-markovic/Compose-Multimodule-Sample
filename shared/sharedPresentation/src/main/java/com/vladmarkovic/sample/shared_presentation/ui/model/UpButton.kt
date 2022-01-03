/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.ui.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.vector.ImageVector
import com.vladmarkovic.sample.shared_presentation.R
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.navigation.CommonNavigationAction.Back

sealed class UpButton(open val icon: ImageVector,
                      @StringRes open val contentDescriptionRes: Int,
                      open val action: () -> Unit) {

    data class CustomButton(override val icon: ImageVector,
                            @StringRes override val contentDescriptionRes: Int,
                            override val action: () -> Unit) :
        UpButton(icon, contentDescriptionRes, action)

    data class BackButton(override val action: () -> Unit) :
        UpButton(Icons.Filled.ArrowBack, R.string.button_back_label, action) {
            constructor(vm: BriefActionViewModel) : this({ vm.navigate(Back) })
        }
}
