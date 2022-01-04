/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_data

import com.vladmarkovic.sample.post_data.PostDatabase.Companion.POSTS_TABLE
import com.vladmarkovic.sample.post_data.model.DataPost
import com.vladmarkovic.sample.post_domain.PostRepository
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.shared_data.model.LastSaved
import com.vladmarkovic.sample.shared_domain.AppSystem
import com.vladmarkovic.sample.shared_domain.model.DataSource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class PostDataRepository @Inject constructor(
    private val postApi: PostApi,
    override val postDao: PostDao,
    override val system: AppSystem,
) : BasePostDataRepository(postDao, system), PostRepository {

    override suspend fun fetchAllPosts(forceFetch: DataSource): List<Post> =
        when (forceFetch) {
            DataSource.REMOTE -> fetchAllPostsFromRemote()
            DataSource.CACHE -> fetchAllPostsFromCache()
            DataSource.UNSPECIFIED -> {
                if (cacheExpired(POSTS_TABLE)) fetchAllPostsFromRemote()
                else fetchAllPostsFromCache()
            }
        }

    override suspend fun deletePost(post: Post) {
        postDao.deletePost(post.id)
    }

    override fun observePostsCache(): Flow<List<Post>> = postDao.observePosts()

    private suspend fun fetchAllPostsFromCache(): List<DataPost> =
        postDao.getAllPosts() ?: emptyList()

    private suspend fun fetchAllPostsFromRemote(): List<DataPost> =
        postApi.fetchAllPosts().also { posts ->
            updateDatabase(posts)
        }

    private suspend fun updateDatabase(posts: List<DataPost>) {
        postDao.deleteAllPosts()
        postDao.deleteAllAuthors()
        postDao.deleteAllLastSaved()
        postDao.insertPosts(posts)
        postDao.updateLastSaved(
            LastSaved(
                what = POSTS_TABLE,
                time = system.currentMillis
            )
        )
    }
}
