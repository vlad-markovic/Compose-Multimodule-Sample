package com.vladmarkovic.sample.common.mvi

/**
 * Events emitted by the view, consumed by the ViewModel, such as click events.
 */
interface ViewEventEmittable<in Event> {
    fun onEvent(event: Event)
}
