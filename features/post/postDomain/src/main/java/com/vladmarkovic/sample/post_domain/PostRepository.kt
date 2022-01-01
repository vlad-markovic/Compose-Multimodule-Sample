package com.vladmarkovic.sample.post_domain

import com.vladmarkovic.sample.post_domain.model.Post

interface PostRepository {

    suspend fun fetchAllPosts(forceRefresh: Boolean = false): List<Post>
}
