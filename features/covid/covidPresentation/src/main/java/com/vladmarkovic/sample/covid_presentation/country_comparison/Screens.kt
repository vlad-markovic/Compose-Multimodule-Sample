/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

@file:Suppress("FunctionName")

package com.vladmarkovic.sample.covid_presentation.country_comparison

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vladmarkovic.sample.covid_domain.model.CountryCovidInfo
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonItem.CountryDetails
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonItem.CountryDetails.Grouped
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonItem.GroupHeader
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonSort.COUNTRY_NAME
import com.vladmarkovic.sample.shared_domain.log.Lumber

@Composable
fun CountryComparisonScreen(
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
        Column(Modifier.padding(8.dp)) {
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

    LazyColumn(state = listState, modifier = Modifier.padding(start = 8.dp, bottom = 48.dp)) {
        items(items.size) { index ->
            when (val item = items[index]) {
                is GroupHeader -> {
                    Text(
                        color = MaterialTheme.colors.secondaryVariant,
                        text = item.continent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }
                is CountryDetails -> {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clickable { onOpenCountryDetail(item.info) }
                    ) {
                        if (sort != COUNTRY_NAME) {
                            if (item is Grouped) {
                                Text(
                                    item.inContinentOrdinal.toString(),
                                    Modifier
                                        .width(40.dp)
                                        .padding(end = 10.dp)
                                )
                            }
                            Text(
                                item.ordinal.toString(),
                                Modifier
                                    .width(40.dp)
                                    .padding(end = 10.dp)
                            )
                            Text(item.sortField,
                                Modifier
                                    .width(90.dp)
                                    .padding(end = 10.dp))
                        }
                        Text(item.info.country, Modifier.padding(end = 10.dp))
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
            modifier = Modifier.padding(8.dp)
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
