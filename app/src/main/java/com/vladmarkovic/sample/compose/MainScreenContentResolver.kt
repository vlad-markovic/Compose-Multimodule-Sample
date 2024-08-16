package com.vladmarkovic.sample.compose

import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.common.mv.action.ViewAction
import com.vladmarkovic.sample.common.navigation.screen.compose.content.ComposeScreenContentResolver
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonScreen
import com.vladmarkovic.sample.covid_presentation.country_info.CountryCovidInfoScreen
import com.vladmarkovic.sample.post_presentation.feed.compose.FeedScreen
import com.vladmarkovic.sample.post_presentation.post.compose.PostScreen
import com.vladmarkovic.sample.shared_domain.screen.MainScreen
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class MainScreenContentResolver @Inject constructor(): ComposeScreenContentResolver<MainScreen> {

    @Composable
    override fun MainScreen.Content(bubbleUp: (ViewAction) -> Unit) = when (this) {
        MainScreen.PostsScreen.FEED_SCREEN -> FeedScreen(bubbleUp)
        MainScreen.PostsScreen.POST_SCREEN -> PostScreen(bubbleUp)
        MainScreen.CovidScreen.COVID_COUNTRY_COMPARISON -> CountryComparisonScreen(bubbleUp)
        MainScreen.CovidScreen.COVID_COUNTRY_INFO -> CountryCovidInfoScreen(bubbleUp)
    }
}
