package com.vladmarkovic.sample.shared_test

import com.vladmarkovic.sample.shared_domain.connectivity.NetworkConnectivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestNetworkConnectivity(
    initialConnectionState: Boolean = false,
    val state: MutableStateFlow<Boolean> = MutableStateFlow(initialConnectionState)
) : NetworkConnectivity {

    override val connectionState: Flow<Boolean> = state

    override val isConnected: Boolean get() = state.value
}
