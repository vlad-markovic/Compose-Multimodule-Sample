package com.vladmarkovic.sample.feed_data

import com.vladmarkovic.sample.feed_data.model.ApiPost
import com.vladmarkovic.sample.feed_domain.PostRepository
import com.vladmarkovic.sample.feed_domain.model.Post
import com.vladmarkovic.sample.shared_data.api.KtorApiService
import io.ktor.client.request.*
import javax.inject.Inject

/** [PostRepository] implementation for fetching a list of [Post]s from the api using ktor. */
class PostApiService @Inject constructor(): KtorApiService(), PostRepository {

    private companion object {
        private const val BASE_URL = "https://jsonplaceholder.typicode.com/posts"
    }

    override suspend fun fetchPosts(): List<Post> = httpApiClient.get<List<ApiPost>>(BASE_URL)
}
