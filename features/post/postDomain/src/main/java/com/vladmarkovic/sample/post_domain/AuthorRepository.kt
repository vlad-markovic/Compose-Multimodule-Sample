package com.vladmarkovic.sample.post_domain

import com.vladmarkovic.sample.post_domain.model.Author

interface AuthorRepository {
    suspend fun fetchAuthor(id: Int): Author
}
