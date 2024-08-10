package com.vladmarkovic.sample.shared_presentation.compose

import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.shared_domain.screen.NavGraphScreen
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.compose.di.ProviderViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface ComposeScreenContentResolver {
    @Composable
    fun NavGraphScreen.Content(bubbleUp: (BriefAction) -> Unit)

    @HiltViewModel
    class Provider @Inject constructor(resolver: ComposeScreenContentResolver)
        : ProviderViewModel<ComposeScreenContentResolver>(resolver)
}
