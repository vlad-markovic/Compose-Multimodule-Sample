package com.vladmarkovic.sample.common.di.model

interface BaseAssistedFactory<T, I> {
    fun create(assistedInput: I): T
}
