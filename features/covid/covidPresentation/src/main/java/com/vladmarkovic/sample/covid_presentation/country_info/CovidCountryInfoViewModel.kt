/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation.country_info

import androidx.lifecycle.SavedStateHandle
import com.vladmarkovic.sample.covid_domain.model.CountryCovidInfo
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.ArgKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class CovidCountryInfoViewModel @Inject constructor(
    private val state: SavedStateHandle
) : BriefActionViewModel() {

    val info: CountryCovidInfo = Json.decodeFromString(
        state.get<String>(ArgKeys.COUNTRY_INFO.name)!!
    )

    override fun onCleared() {
        super.onCleared()

        state[ArgKeys.COUNTRY_INFO.name] = null
    }
}
