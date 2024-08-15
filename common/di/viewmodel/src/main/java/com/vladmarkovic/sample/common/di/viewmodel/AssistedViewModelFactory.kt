package com.vladmarkovic.sample.common.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vladmarkovic.sample.common.di.model.BaseAssistedFactory

interface AssistedViewModelFactory<VM : ViewModel, I> : ViewModelProvider.Factory, BaseAssistedFactory<VM, I> {
    override fun create(assistedInput: I): VM
}

fun <VM : ViewModel, I> viewModelProviderFactory(
    assistedFactory: AssistedViewModelFactory<VM, I>,
    assistedInput: I
): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    override fun <V : ViewModel> create(modelClass: Class<V>): V {
        @Suppress("UNCHECKED_CAST")
        return assistedFactory.create(assistedInput) as V
    }
}
