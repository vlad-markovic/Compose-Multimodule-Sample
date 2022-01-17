/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation.country_comparison

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.vladmarkovic.sample.covid_domain.CovidInfoRepo
import com.vladmarkovic.sample.covid_domain.model.CountryCovidInfo
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonSort.COUNTRY_NAME
import com.vladmarkovic.sample.covid_presentation.country_info.mapToItems
import com.vladmarkovic.sample.shared_domain.log.Lumber
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CountryComparisonViewModel @Inject constructor(private val covidInfoRepo: CovidInfoRepo) : BriefActionViewModel() {

    private val _showLoading: MutableState<Boolean> = mutableStateOf(false)
    val showLoading: State<Boolean> = _showLoading

    private val _covidInfo: MutableState<List<CountryCovidInfo>> = mutableStateOf(listOf())
    private val _items: MutableState<List<CountryComparisonItem>> = mutableStateOf(listOf())
    val items: State<List<CountryComparisonItem>> = _items

    private val _sortBy: MutableState<CountryComparisonSort> = mutableStateOf(COUNTRY_NAME)
    val sortBy: State<CountryComparisonSort> = _sortBy

    private val _sortAscending: MutableState<Boolean> = mutableStateOf(true)
    val sortAscending: State<Boolean> = _sortAscending

    private val _groupByContinent: MutableState<Boolean> = mutableStateOf(false)
    val groupByContinent: State<Boolean> = _groupByContinent

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

            withContext(Dispatchers.Main) {
                _showLoading.value = false
                _covidInfo.value = covidInfoList
                _items.value = items
            }
        }
    }

    private fun updateSortOrder() {
        _items.value = getResortedItems(_covidInfo.value)
    }

    private fun getResortedItems(covidInfoList: List<CountryCovidInfo>): List<CountryComparisonItem> =
        covidInfoList.mapToItems(groupByContinent.value, sortBy.value, sortAscending.value)
}
