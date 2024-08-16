package com.vladmarkovic.sample.common.navigation.screen.compose.content

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class ComposeScreenContentResolverProvider @Inject constructor(
    val resolver: ComposeScreenContentResolver
) : ViewModel()

@Composable
fun injectScreenContentResolver(): ComposeScreenContentResolver =
    hiltViewModel<ComposeScreenContentResolverProvider>().resolver
