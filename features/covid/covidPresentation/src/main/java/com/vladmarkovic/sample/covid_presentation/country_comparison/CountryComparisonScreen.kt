/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

@file:Suppress("FunctionName")

package com.vladmarkovic.sample.covid_presentation.country_comparison

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vladmarkovic.sample.common.android.model.StrOrRes
import com.vladmarkovic.sample.common.compose.util.lifecycleAwareValue
import com.vladmarkovic.sample.common.logging.Lumber
import com.vladmarkovic.sample.common.mv.action.compose.actionViewModel
import com.vladmarkovic.sample.common.mv.action.ViewAction
import com.vladmarkovic.sample.common.mv.action.navigate
import com.vladmarkovic.sample.covid_domain.model.CountryCovidInfo
import com.vladmarkovic.sample.covid_presentation.R
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonItem.CountryDetails
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonItem.CountryDetails.Grouped
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonItem.GroupHeader
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonSort.COUNTRY_NAME
import com.vladmarkovic.sample.covid_presentation.navigation.ToCountryInfoScreen
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.model.ScaffoldData
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultTopBar
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.model.TopBarData
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.model.UpButton
import com.vladmarkovic.sample.shared_presentation.ui.model.defaultDrawerItems

@Composable
fun CountryComparisonScreen(
    bubbleUp: (ViewAction) -> Unit,
    viewModel: CountryComparisonViewModel = actionViewModel<CountryComparisonViewModel>(bubbleUp)
) {
    LaunchedEffect(Unit) {
        bubbleUp(ScaffoldData(defaultDrawerItems(viewModel)))
    }

    Column(Modifier.fillMaxSize()) {
        DefaultTopBar(
            TopBarData(
                StrOrRes.res(R.string.country_comparison_screen_title),
                UpButton.DrawerButton(viewModel),
                listOf(
                    CountryComparisonMenu.GroupByContinent(viewModel::groupByContinent, viewModel.groupByContinent),
                    CountryComparisonMenu.Sort(viewModel::sortAscending, viewModel.sortAscending)
                )
            )
        )

        CountryComparisonScreen(
            showLoading = viewModel.showLoading.lifecycleAwareValue,
            items = viewModel.items.lifecycleAwareValue,
            sortBy = viewModel.sortBy.lifecycleAwareValue,
            onSortByChanged = viewModel::sortBy,
            onOpenCountryDetail = { countryCovidInfo ->
                viewModel.navigate(ToCountryInfoScreen(countryCovidInfo))
            }
        )
    }
}

@Composable
private fun CountryComparisonScreen(
    showLoading: Boolean,
    items: List<CountryComparisonItem>,
    sortBy: CountryComparisonSort,
    onSortByChanged: (CountryComparisonSort) -> Unit,
    onOpenCountryDetail: (CountryCovidInfo) -> Unit
) {
    if (showLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
        Column((Modifier.fillMaxSize())) {
            SortByDropdown(sortBy, onSortByChanged)
            CountryList(items, sortBy, onOpenCountryDetail)
        }
    }
}

@Composable
fun CountryList(
    items: List<CountryComparisonItem>,
    sort: CountryComparisonSort,
    onOpenCountryDetail: (CountryCovidInfo) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(state = listState) {
        items(items.size) { index ->
            when (val item = items[index]) {
                is GroupHeader -> {
                    Text(
                        color = MaterialTheme.colors.secondaryVariant,
                        text = item.continent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    )
                }

                is CountryDetails -> {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clickable { onOpenCountryDetail(item.info) }
                    ) {
                        if (sort != COUNTRY_NAME) {
                            if (item is Grouped) {
                                Text(
                                    item.inContinentOrdinal.toString(),
                                    Modifier
                                        .width(40.dp)
                                        .padding(end = 12.dp)
                                )
                            }
                            Text(
                                item.ordinal.toString(),
                                Modifier
                                    .width(40.dp)
                                    .padding(end = 12.dp)
                            )
                            Text(
                                item.sortField,
                                Modifier
                                    .width(90.dp)
                                    .padding(end = 12.dp)
                            )
                        }
                        Text(item.info.country, Modifier.padding(end = 12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SortByDropdown(sort: CountryComparisonSort, onSortChanged: (CountryComparisonSort) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(
            onClick = { expanded = !expanded },
            modifier = Modifier.padding(top = 10.dp, start = 10.dp)
        ) {
            Text("Sort by: ${sort.display}")
            Icon(Icons.Filled.ArrowDropDown, null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                Lumber.v("onDismissRequest")
                expanded = false
            }
        ) {
            CountryComparisonSort.entries.forEach { sortItem ->
                DropdownMenuItem({
                    expanded = false
                    Lumber.v("CLICK ITEM: $sortItem")
                    if (sortItem != sort) onSortChanged(sortItem)
                }) {
                    val modifier =
                        if (sortItem != sort) Modifier
                        else Modifier.background(MaterialTheme.colors.background)

                    Text(sortItem.display, modifier)
                }
            }
        }
    }
}
