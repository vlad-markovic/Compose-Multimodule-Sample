/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.briefaction

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/** Base [ViewModel] which can send [BriefAction]s. */
@HiltViewModel
open class BriefActionViewModel @Inject constructor() : ViewModel(), BriefActionable by BriefActionerHolder()