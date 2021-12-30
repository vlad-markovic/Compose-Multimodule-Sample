package com.vladmarkovic.sample.post_domain

import com.vladmarkovic.sample.post_domain.model.Post

interface PostRepository {

    suspend fun fetchPosts(): List<Post>
}
