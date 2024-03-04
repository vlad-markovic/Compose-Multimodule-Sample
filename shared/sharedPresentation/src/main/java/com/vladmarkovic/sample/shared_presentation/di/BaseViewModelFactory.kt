package com.vladmarkovic.sample.shared_presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

interface BaseAssistedFactory<T, I> {
    fun create(assistedInput: I): T
}

interface AssistedViewModelFactory<VM : ViewModel, I> : ViewModelProvider.Factory, BaseAssistedFactory<VM, I> {
    override fun create(assistedInput: I): VM
}

fun <VM: ViewModel, I> viewModelProviderFactory(
    assistedFactory: AssistedViewModelFactory<VM, I>,
    assistedInput: I
) : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    override fun <V : ViewModel> create(modelClass: Class<V>): V {
        @Suppress("UNCHECKED_CAST")
        return assistedFactory.create(assistedInput) as V
    }
}
