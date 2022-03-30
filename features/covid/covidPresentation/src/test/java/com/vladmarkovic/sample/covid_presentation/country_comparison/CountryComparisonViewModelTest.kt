/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation.country_comparison

import com.vladmarkovic.sample.covid_domain.CovidInfoRepo
import com.vladmarkovic.sample.shared_test.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class CountryComparisonViewModelTest {

    companion object {

        @JvmField
        @RegisterExtension
        @ExperimentalCoroutinesApi
        @Suppress("Unused")
        val testSetupExtension: CustomizableEachTestSetupExtension =
            CustomizableEachTestSetupExtension(UnconfinedTestDispatcher())
                .setupLiveData()
                .setupCoroutines()
                .setupLogger()
    }

    private val mockCovidInfoRepo = mockk<CovidInfoRepo>()

    private lateinit var viewModel: CountryComparisonViewModel

    @BeforeEach
    fun setup() {
        coEvery { mockCovidInfoRepo.getAffectedCountries() }.returns(testCountryCovidInfoData)

        viewModel = CountryComparisonViewModel(mockCovidInfoRepo)
    }

    @Test
    @DisplayName("Given landing on covid country list screen, when data is fetched, it shows fetched data")
    fun testInitialState() {
        viewModel.showLoading.assertValueEquals(false)
        viewModel.sortBy.assertValueEquals(CountryComparisonSort.COUNTRY_NAME)
        viewModel.sortAscending.assertValueEquals(true)
        viewModel.groupByContinent.assertValueEquals(false)
        viewModel.items.assertValueEquals(testCountryCovidItemsNotGroupedSortedByName)
    }
}
