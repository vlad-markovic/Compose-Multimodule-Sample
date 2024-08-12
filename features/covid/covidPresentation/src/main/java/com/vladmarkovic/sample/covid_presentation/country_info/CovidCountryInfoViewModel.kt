/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation.country_info

import androidx.lifecycle.SavedStateHandle
import com.vladmarkovic.sample.covid_domain.model.CountryCovidInfo
import com.vladmarkovic.sample.shared_presentation.viewaction.ActionViewModel
import com.vladmarkovic.sample.shared_presentation.screen.ScreenArgNames
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class CovidCountryInfoViewModel @Inject constructor(
    private val state: SavedStateHandle
) : ActionViewModel() {

    val info: CountryCovidInfo = Json.decodeFromString(
        state.get<String>(ScreenArgNames.COUNTRY_INFO.name)!!
    )

    override fun onCleared() {
        super.onCleared()

        state[ScreenArgNames.COUNTRY_INFO.name] = null
    }
}
