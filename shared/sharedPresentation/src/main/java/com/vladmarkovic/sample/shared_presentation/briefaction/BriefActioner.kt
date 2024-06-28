/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.briefaction

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onSubscription
import java.util.LinkedList
import java.util.Queue
import javax.inject.Inject

/** Manages "sending" of [BriefAction]s. */
@ViewModelScoped
class BriefActioner @Inject constructor() : BriefActionable {

    // For ensuring ViewModels are able to send actions even from init (before collection is set up)
    private val initialCache: Queue<BriefAction> = LinkedList()

    private val _action = MutableSharedFlow<BriefAction>()
    override val action: Flow<BriefAction> = _action
        .onSubscription {
            initialCache.forEach { emit(it) }
            initialCache.clear()
        }

    override suspend fun actionAsync(action: BriefAction) {
        _action.emit(action)
        if (_action.subscriptionCount.value == 0) {
            initialCache.offer(action)
            // allow only actions set 100ms before subscription to be cached.
            delay(100)
            initialCache.clear()
        }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class BriefActionerModule {

    @ViewModelScoped
    @Provides
    fun provideBriefActionable(actioner: BriefActioner): BriefActionable = actioner
}
