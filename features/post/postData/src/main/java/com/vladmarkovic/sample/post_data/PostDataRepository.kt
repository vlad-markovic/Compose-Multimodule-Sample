package com.vladmarkovic.sample.post_data

import com.vladmarkovic.sample.post_data.model.DataPost
import com.vladmarkovic.sample.post_domain.PostRepository
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.shared_data.model.LastSaved
import com.vladmarkovic.sample.shared_domain.AppSystem
import dagger.hilt.android.scopes.ViewModelScoped
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ViewModelScoped
class PostDataRepository @Inject constructor(
    private val postApi: PostApi,
    private val postDao: PostDao,
    private val system: AppSystem,
) : PostRepository {

    companion object {
        val CACHE_EXPIRY_MILLIS = TimeUnit.MINUTES.toMillis(5)
    }

    override suspend fun fetchAllPosts(forceRefresh: Boolean): List<Post> =
        if (forceRefresh || cacheExpired()) fetchAllPostsFromApi()
        else postDao.getAllPosts()

    private suspend fun cacheExpired(): Boolean =
        lastSaved() <= system.currentMillis - CACHE_EXPIRY_MILLIS

    private suspend fun lastSaved(): Long =
        postDao.getLastSaved(PostDatabase.POSTS_TABLE)?.time ?: 0L

    private suspend fun fetchAllPostsFromApi(): List<DataPost> =
        postApi.fetchAllPosts().also { posts ->
            updateDatabase(posts)
        }

    private suspend fun updateDatabase(posts: List<DataPost>) {
        postDao.deleteAllPosts()
        postDao.insertPosts(posts)
        postDao.updateLastSaved(
            LastSaved(
                what = PostDatabase.POSTS_TABLE,
                time = system.currentMillis
            )
        )
    }
}
