/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_domain.connectivity

import kotlinx.coroutines.flow.Flow

interface NetworkConnectivity {

    val isConnected: Boolean

    val connectionState: Flow<Boolean>
}
