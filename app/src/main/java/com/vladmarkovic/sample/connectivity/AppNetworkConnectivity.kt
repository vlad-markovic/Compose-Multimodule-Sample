/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.vladmarkovic.sample.shared_domain.connectivity.NetworkConnectivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class AppNetworkConnectivity(context: Context) : NetworkConnectivity {

    private val _state = MutableStateFlow(false)

    override val connectionState: Flow<Boolean> = _state

    override val isConnected: Boolean get() = _state.value

    init {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        setup(connectivityManager)
    }

    private fun setup(connectivityManager: ConnectivityManager) {
        connectivityManager.registerDefaultNetworkCallback(
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    _state.value = true
                }

                override fun onLost(network: Network) {
                    _state.value = false
                }
            }
        )
    }
}
