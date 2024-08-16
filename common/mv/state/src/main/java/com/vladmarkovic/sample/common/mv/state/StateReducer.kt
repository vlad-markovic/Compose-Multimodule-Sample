package com.vladmarkovic.sample.common.mv.state

typealias StateReducer<State, Change> = (State, Change) -> State
