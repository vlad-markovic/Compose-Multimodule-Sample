/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.nav

import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonScreen
import com.vladmarkovic.sample.covid_presentation.country_info.CountryCovidInfoScreen
import com.vladmarkovic.sample.post_presentation.feed.compose.FeedScreen
import com.vladmarkovic.sample.post_presentation.post.compose.PostScreen
import com.vladmarkovic.sample.settings_presentation.main.SettingsMainScreen
import com.vladmarkovic.sample.settings_presentation.screen_two.SettingsTwoScreen
import com.vladmarkovic.sample.shared_domain.screen.MainScreen
import com.vladmarkovic.sample.shared_domain.screen.Screen
import com.vladmarkovic.sample.shared_domain.screen.SettingsScreen
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.compose.ComposeScreenContentResolver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComposeScreenContentResolverImpl @Inject constructor(): ComposeScreenContentResolver {
    @Composable
    override fun Screen.Content(bubbleUp: (BriefAction) -> Unit) = when (this) {
        MainScreen.PostsScreen.FEED_SCREEN -> FeedScreen(bubbleUp)
        MainScreen.PostsScreen.POST_SCREEN -> PostScreen(bubbleUp)
        MainScreen.CovidScreen.COVID_COUNTRY_COMPARISON -> CountryComparisonScreen(bubbleUp)
        MainScreen.CovidScreen.COVID_COUNTRY_INFO -> CountryCovidInfoScreen(bubbleUp)
        SettingsScreen.MAIN -> SettingsMainScreen(bubbleUp)
        SettingsScreen.SECOND -> SettingsTwoScreen(bubbleUp)
    }
}
