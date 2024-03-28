package com.vladmarkovic.sample.post_presentation.feed

import com.vladmarkovic.sample.post_domain.AuthorRepository
import com.vladmarkovic.sample.post_domain.model.Author
import kotlinx.coroutines.delay

class FakeAuthorRepository(private val t: Throwable? = null) : AuthorRepository {

    companion object {
        const val FAKE_FETCH_DELAY = 3L
    }

    override suspend fun fetchAuthor(id: Int): Author {
        t?.let { throw it } ?: delay(FAKE_FETCH_DELAY - 1)
        return fakeAuthor
    }
}