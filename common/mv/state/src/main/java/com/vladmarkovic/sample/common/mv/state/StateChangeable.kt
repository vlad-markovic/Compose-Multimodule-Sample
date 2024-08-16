package com.vladmarkovic.sample.common.mv.state

interface StateChangeable<out State, in Change> {
    fun changeState(change: Change)
}
