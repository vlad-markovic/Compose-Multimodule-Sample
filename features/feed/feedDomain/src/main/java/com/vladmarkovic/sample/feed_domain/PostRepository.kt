package com.vladmarkovic.sample.feed_domain

import com.vladmarkovic.sample.feed_domain.model.Post

interface PostRepository {

    suspend fun fetchPosts(): List<Post>
}
