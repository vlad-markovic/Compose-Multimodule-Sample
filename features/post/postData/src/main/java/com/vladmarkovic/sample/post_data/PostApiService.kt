package com.vladmarkovic.sample.post_data

import com.vladmarkovic.sample.post_data.model.DataAuthor
import com.vladmarkovic.sample.post_data.model.DataPost
import com.vladmarkovic.sample.post_domain.PostRepository
import com.vladmarkovic.sample.post_domain.model.Post
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.*
import io.ktor.client.request.*
import javax.inject.Inject

/** [PostRepository] implementation for fetching a list of [Post]s from the api using ktor. */
@ViewModelScoped
class PostApiService @Inject constructor(private val httpClient: HttpClient) : PostApi {

    private companion object {
        private const val BASE_URL = "https://jsonplaceholder.typicode.com"
    }

    override suspend fun fetchAllPosts(): List<DataPost> =
        httpClient.get("$BASE_URL/posts")

    override suspend fun fetchAuthor(id: Int): DataAuthor =
        httpClient.get("$BASE_URL/users/$id")
}
