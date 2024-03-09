/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation.country_comparison

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.covid_presentation.R.string.country_comparison_screen_title
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonMenu.GroupByContinent
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonMenu.Sort
import com.vladmarkovic.sample.covid_presentation.navigation.ToCountryInfoScreen
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.briefaction.navigate
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposer
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.CovidScreen
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.drawer.defaultDrawerItems
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.safeValue
import java.util.Optional
import javax.inject.Inject

/** Defines Compose UI and elements for Covid country comparison screen. */
class CovidCountryComparisonScreenComposer @Inject constructor() : ScreenComposer<CountryComparisonViewModel>() {

    override val screen: Screen = CovidScreen.COVID_COUNTRY_COMPARISON

    @Composable
    override fun viewModel(bubbleUp: (BriefAction) -> Unit): CountryComparisonViewModel =
        actionViewModel<CountryComparisonViewModel>(bubbleUp)

    override fun topBarChange(viewModel: CountryComparisonViewModel): Optional<@Composable (() -> Unit)> =
        defaultTopBarChange(
            title = StrOrRes.res(country_comparison_screen_title),
            upButton = UpButton.BackButton(viewModel),
            menuItems = listOf(
                GroupByContinent(viewModel::groupByContinent, viewModel.groupByContinent),
                Sort(viewModel::sortAscending, viewModel.sortAscending)
            )
        )

    override fun drawerChange(viewModel: CountryComparisonViewModel):
            Optional<@Composable (ColumnScope.() -> Unit)> = defaultDrawerChange(defaultDrawerItems(viewModel))

    @Composable
    override fun Content(viewModel: CountryComparisonViewModel) {
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
