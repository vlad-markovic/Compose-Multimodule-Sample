/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation.country_comparison

import androidx.lifecycle.viewModelScope
import com.vladmarkovic.sample.common.logging.Lumber
import com.vladmarkovic.sample.common.view.action.ActionViewModel
import com.vladmarkovic.sample.covid_domain.CovidInfoRepo
import com.vladmarkovic.sample.covid_domain.model.CountryCovidInfo
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonSort.COUNTRY_NAME
import com.vladmarkovic.sample.covid_presentation.country_info.mapToItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryComparisonViewModel @Inject constructor(private val covidInfoRepo: CovidInfoRepo) : ActionViewModel() {

    private val _showLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showLoading: StateFlow<Boolean> = _showLoading.asStateFlow()

    private val _covidInfo: MutableStateFlow<List<CountryCovidInfo>> = MutableStateFlow(listOf())
    private val _items: MutableStateFlow<List<CountryComparisonItem>> = MutableStateFlow(listOf())
    val items: StateFlow<List<CountryComparisonItem>> = _items.asStateFlow()

    private val _sortBy: MutableStateFlow<CountryComparisonSort> = MutableStateFlow(COUNTRY_NAME)
    val sortBy: StateFlow<CountryComparisonSort> = _sortBy.asStateFlow()

    private val _sortAscending: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val sortAscending: StateFlow<Boolean> = _sortAscending.asStateFlow()

    private val _groupByContinent: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val groupByContinent: StateFlow<Boolean> = _groupByContinent.asStateFlow()

    fun sortBy(sort: CountryComparisonSort) {
        _sortBy.value = sort
        updateSortOrder()
    }

    fun groupByContinent(group: Boolean) {
        _groupByContinent.value = group
        updateSortOrder()
    }

    fun sortAscending(yes: Boolean) {
        _sortAscending.value = yes
        updateSortOrder()
    }

    init {
        _showLoading.value = true
        update()
    }

    private fun update() {
        viewModelScope.launch {
            val covidInfoList = try {
                covidInfoRepo.getAffectedCountries()
            } catch (e: Exception) {
                Lumber.e(e, "Error fetching covid data")
                emptyList()
            }

            val items = getResortedItems(covidInfoList)

            _showLoading.value = false
            _covidInfo.value = covidInfoList
            _items.value = items
        }
    }

    private fun updateSortOrder() {
        _items.value = getResortedItems(_covidInfo.value)
    }

    private fun getResortedItems(covidInfoList: List<CountryCovidInfo>): List<CountryComparisonItem> =
        covidInfoList.mapToItems(groupByContinent.value, sortBy.value, sortAscending.value)
}
