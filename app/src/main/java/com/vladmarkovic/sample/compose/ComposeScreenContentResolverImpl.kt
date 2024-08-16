package com.vladmarkovic.sample.compose

import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.common.navigation.screen.model.NavGraphScreen
import com.vladmarkovic.sample.common.view.action.ViewAction
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonScreen
import com.vladmarkovic.sample.covid_presentation.country_info.CountryCovidInfoScreen
import com.vladmarkovic.sample.post_presentation.feed.compose.FeedScreen
import com.vladmarkovic.sample.post_presentation.post.compose.PostScreen
import com.vladmarkovic.sample.settings_presentation.main.SettingsMainScreen
import com.vladmarkovic.sample.settings_presentation.screen_two.SettingsTwoScreen
import com.vladmarkovic.sample.shared_domain.screen.MainScreen
import com.vladmarkovic.sample.shared_domain.screen.SettingsScreen
import com.vladmarkovic.sample.shared_presentation.compose.ComposeScreenContentResolver
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ComposeScreenContentResolverImpl @Inject constructor(): ComposeScreenContentResolver {
    @Composable
    override fun NavGraphScreen.Content(bubbleUp: (ViewAction) -> Unit) = when (this) {
        MainScreen.PostsScreen.FEED_SCREEN -> FeedScreen(bubbleUp)
        MainScreen.PostsScreen.POST_SCREEN -> PostScreen(bubbleUp)
        MainScreen.CovidScreen.COVID_COUNTRY_COMPARISON -> CountryComparisonScreen(bubbleUp)
        MainScreen.CovidScreen.COVID_COUNTRY_INFO -> CountryCovidInfoScreen(bubbleUp)
        SettingsScreen.MAIN -> SettingsMainScreen(bubbleUp)
        SettingsScreen.SECOND -> SettingsTwoScreen(bubbleUp)
        else -> throw IllegalArgumentException("Content not provided for screen: $this")
    }
}