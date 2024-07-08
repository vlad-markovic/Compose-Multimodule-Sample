package com.vladmarkovic.sample.shared_presentation.compose.di

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel


abstract class ProviderViewModel<T>(val value: T) : ViewModel()

@Composable
inline fun <reified T, reified VM : ProviderViewModel<T>> inject(): T = hiltViewModel<VM>().value
