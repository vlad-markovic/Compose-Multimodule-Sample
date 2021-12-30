package com.vladmarkovic.sample.shared_presentation.briefaction

import androidx.annotation.UiThread
import androidx.collection.ArraySet
import androidx.lifecycle.Lifecycle.State.DESTROYED
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Stateless LiveData version, which allows observing of a [BriefAction] only once.
 * @param allowMultipleObservers - can allow for multiple observer or only one (default).
 * Adapted from https://github.com/hadilq/LiveEvent/blob/master/lib/src/main/java/com/hadilq/liveevent/LiveEvent.kt
 */
abstract class LiveAction<A: BriefAction>(
    private val allowMultipleObservers: Boolean = false
): LiveData<A>(null) {

    private val observers = ArraySet<ObserverWrapper<in A>>()

    override fun observeForever(observer: Observer<in A>) {
        if (allowMultipleObservers || observers.isEmpty()) {
            synchronized(observers) {
                val observerWrapper = ObserverWrapper(observer, ::resetValue)
                if (!observers.contains(observerWrapper)) {
                    observers.add(observerWrapper)
                }
                super.observeForever(observer)
            }
        }
    }

    @UiThread
    override fun observe(lifecycleOwner: LifecycleOwner, observer: Observer<in A>) {
        synchronized(observers) {
            if (allowMultipleObservers || observers.isEmpty()) {
                val observerWrapper = ObserverWrapper(observer, ::resetValue)
                if (!observers.contains(observerWrapper) && lifecycleOwner.lifecycle.currentState != DESTROYED) {
                    observers.add(observerWrapper)
                }
                super.observe(lifecycleOwner, observerWrapper)
            }
        }
    }

    @UiThread
    override fun removeObserver(observer: Observer<in A>) {
        synchronized(observers) {
            if (!observers.remove(observer)) {
                val iterator = observers.iterator()
                while (iterator.hasNext()) {
                    if (iterator.next().observer == observer) {
                        iterator.remove()
                        break
                    }
                }
            }
            super.removeObserver(observer)
        }
    }

    @UiThread
    override fun setValue(action: A?) {
        synchronized(observers) {
            if (action != null) {
                observers.forEach { it.resetProcessed() }
            }

            super.setValue(action)
        }
    }

    private fun resetValue() {
        synchronized(observers) {
            if (value != null) {
                value = null
            }
        }
    }

    private class ObserverWrapper<A: BriefAction>(val observer: Observer<in A>,
                                                  val resetValue: () -> Unit) : Observer<A> {

        // Initialise to true not to consume any events on subscribing.
        private val processed: AtomicBoolean = AtomicBoolean(true)

        override fun onChanged(action: A?) {
            if (!processed.getAndSet(true)) {
                action?.let {
                    observer.onChanged(it)
                    resetValue()
                }
            }
        }

        fun resetProcessed() {
            processed.set(false)
        }
    }
}
