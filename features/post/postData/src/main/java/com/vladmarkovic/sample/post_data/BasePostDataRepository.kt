/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_data

import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.Companion.PROTECTED
import com.vladmarkovic.sample.shared_domain.AppSystem
import java.util.concurrent.TimeUnit

abstract class BasePostDataRepository(
    protected open val postDao: PostDao,
    protected open val system: AppSystem,
) {

    companion object {
        val CACHE_EXPIRY_MILLIS = TimeUnit.MINUTES.toMillis(5)
    }

    @VisibleForTesting(otherwise = PROTECTED)
    suspend fun cacheExpired(tableName: String): Boolean =
        lastSaved(tableName) <= system.currentMillis - CACHE_EXPIRY_MILLIS

    private suspend fun lastSaved(tableName: String): Long =
        postDao.getLastSaved(tableName)?.time ?: 0L
}
