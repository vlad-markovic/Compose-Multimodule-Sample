/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation.country_comparison

import androidx.compose.runtime.State
import com.vladmarkovic.sample.covid_presentation.R.string
import com.vladmarkovic.sample.shared_presentation.ui.model.MenuItem

sealed class CountryComparisonMenu {

    data class GroupByContinent(
        override val onCheckedChange: (Boolean) -> Unit,
        override val checked: State<Boolean>
    ) : CountryComparisonMenu(), MenuItem.Toggle {
        override val checkedTextRes: Int = string.menu_group_by_continent_checked
        override val uncheckedTextRes: Int = string.menu_group_by_continent_unchecked
    }

    data class Sort(
        override val onCheckedChange: (Boolean) -> Unit,
        override val checked: State<Boolean>
    ) : CountryComparisonMenu(), MenuItem.Toggle {
        override val checkedTextRes: Int = string.menu_sort_desc
        override val uncheckedTextRes: Int = string.menu_sort_asc
    }
}
