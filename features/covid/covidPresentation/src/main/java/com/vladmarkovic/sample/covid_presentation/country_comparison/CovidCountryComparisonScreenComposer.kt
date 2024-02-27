/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation.country_comparison

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.vladmarkovic.sample.covid_presentation.R.string.country_comparison_screen_title
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonMenu.GroupByContinent
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonMenu.Sort
import com.vladmarkovic.sample.covid_presentation.navigation.ToCountryInfoScreen
import com.vladmarkovic.sample.shared_presentation.briefaction.navigate
import com.vladmarkovic.sample.shared_presentation.composer.ContentArgs
import com.vladmarkovic.sample.shared_presentation.composer.DrawerScreenComposer
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.CovidScreen
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.drawer.DrawerItem
import com.vladmarkovic.sample.shared_presentation.ui.drawer.defaultDrawerItems
import com.vladmarkovic.sample.shared_presentation.ui.model.MenuItem
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.safeValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/** Defines Compose UI and elements for Covid country comparison screen. */
class CovidCountryComparisonScreenComposer @Inject constructor() : DrawerScreenComposer() {

    override val screen: Screen = CovidScreen.COVID_COUNTRY_COMPARISON

    override val screenTitle: MutableStateFlow<StrOrRes> = titleFromRes(country_comparison_screen_title)

    override val menuItems: StateFlow<Array<MenuItem>> get() = _menuItems.asStateFlow()
    override val drawerItems: StateFlow<List<DrawerItem>> get() = _drawerItems.asStateFlow()

    private val _menuItems: MutableStateFlow<Array<MenuItem>> = MutableStateFlow(arrayOf())
    private val _drawerItems: MutableStateFlow<List<DrawerItem>> = MutableStateFlow(emptyList())

    @Composable
    override fun Content(contentArgs: ContentArgs) {
        super.Content(contentArgs)

        val viewModel: CountryComparisonViewModel = actionViewModel(contentArgs)

        LaunchedEffect(key1 = contentArgs) {
            _menuItems.value = arrayOf(
                GroupByContinent(viewModel::groupByContinent, viewModel.groupByContinent),
                Sort(viewModel::sortAscending, viewModel.sortAscending)
            )
            _drawerItems.value = defaultDrawerItems(viewModel)
        }

        CountryComparisonScreen(
            showLoading = viewModel.showLoading.safeValue,
            items = viewModel.items.safeValue,
            sortBy = viewModel.sortBy.safeValue,
            onSortByChanged = viewModel::sortBy,
            onOpenCountryDetail = { countryCovidInfo ->
                viewModel.navigate(ToCountryInfoScreen(countryCovidInfo))
            }
        )
    }
}
