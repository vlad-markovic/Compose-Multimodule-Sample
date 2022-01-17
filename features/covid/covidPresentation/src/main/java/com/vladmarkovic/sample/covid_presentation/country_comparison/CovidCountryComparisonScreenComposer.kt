/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation.country_comparison

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.vladmarkovic.sample.covid_presentation.R
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonMenu.GroupByContinent
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonMenu.Sort
import com.vladmarkovic.sample.covid_presentation.navigation.ToCountryInfoScreen
import com.vladmarkovic.sample.shared_presentation.composer.ContentArgs
import com.vladmarkovic.sample.shared_presentation.composer.DrawerScreenComposer
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.CovidScreen
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.drawer.DrawerItem
import com.vladmarkovic.sample.shared_presentation.ui.drawer.defaultDrawerItems
import com.vladmarkovic.sample.shared_presentation.ui.model.MenuItem
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import javax.inject.Inject

/** Defines Compose UI and elements for Covid country comparison screen. */
class CovidCountryComparisonScreenComposer @Inject constructor() : DrawerScreenComposer() {

    override val screen: Screen = CovidScreen.COVID_COUNTRY_COMPARISON

    override val screenTitle: MutableState<StrOrRes> =
        titleFromRes(R.string.country_comparison_screen_title)

    override val menuItems: State<Array<MenuItem>> get() = _menuItems
    override val drawerItems: State<List<DrawerItem>> get() = _drawerItems

    private val _menuItems: MutableState<Array<MenuItem>> = mutableStateOf(arrayOf())
    private val _drawerItems: MutableState<List<DrawerItem>> = mutableStateOf(emptyList())

    @Composable
    override fun Content(contentArgs: ContentArgs) {
        super.Content(contentArgs)

        val viewModel: CountryComparisonViewModel = actionViewModel(contentArgs)

        _menuItems.value = arrayOf(
            GroupByContinent(viewModel::groupByContinent, viewModel.groupByContinent),
            Sort(viewModel::sortAscending, viewModel.sortAscending)
        )

        _drawerItems.value = defaultDrawerItems(viewModel)

        CountryComparisonScreen(
            showLoading = viewModel.showLoading,
            _items = viewModel.items,
            _sortBy = viewModel.sortBy,
            onSortByChanged = viewModel::sortBy,
            onOpenCountryDetail = { countryCovidInfo ->
                viewModel.navigate(ToCountryInfoScreen(countryCovidInfo))
            }
        )
    }
}
