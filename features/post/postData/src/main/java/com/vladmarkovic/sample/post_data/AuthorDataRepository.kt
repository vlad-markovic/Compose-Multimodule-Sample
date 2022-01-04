/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_data

import com.vladmarkovic.sample.post_data.PostDatabase.Companion.AUTHORS_TABLE
import com.vladmarkovic.sample.post_data.model.DataAuthor
import com.vladmarkovic.sample.post_domain.AuthorRepository
import com.vladmarkovic.sample.post_domain.model.Author
import com.vladmarkovic.sample.shared_data.model.LastSaved
import com.vladmarkovic.sample.shared_domain.AppSystem
import javax.inject.Inject

class AuthorDataRepository @Inject constructor(
    private val postApi: PostApi,
    override val postDao: PostDao,
    override val system: AppSystem,
) : BasePostDataRepository(postDao, system), AuthorRepository {

    override suspend fun fetchAuthor(id: Int): Author =
        if (cacheExpired(AUTHORS_TABLE)) fetchAuthorFromApi(id)
        else postDao.getAuthor(id) ?: fetchAuthorFromApi(id)

    private suspend fun fetchAuthorFromApi(id: Int): DataAuthor =
        postApi.fetchAuthor(id).also { author ->
            updateDatabase(author)
        }

    private suspend fun updateDatabase(author: DataAuthor) {
        postDao.insertAuthor(author)
        postDao.updateLastSaved(
            LastSaved(
                what = AUTHORS_TABLE,
                time = system.currentMillis
            )
        )
    }
}
