package com.vladmarkovic.sample.post_data

import com.vladmarkovic.sample.post_data.model.DataAuthor
import com.vladmarkovic.sample.post_data.model.DataPost
import com.vladmarkovic.sample.post_domain.AuthorRepository
import com.vladmarkovic.sample.post_domain.PostRepository
import com.vladmarkovic.sample.post_domain.model.Author
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
) : PostRepository, AuthorRepository {

    companion object {
        val CACHE_EXPIRY_MILLIS = TimeUnit.MINUTES.toMillis(5)
    }

    // region posts
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
        postDao.deleteAllAuthors()
        postDao.insertPosts(posts)
        postDao.updateLastSaved(
            LastSaved(
                what = PostDatabase.POSTS_TABLE,
                time = system.currentMillis
            )
        )
    }
    // endregion posts

    // region author
    override suspend fun fetchAuthor(id: Int): Author =
        if (cacheExpired()) fetchAuthorFromApi(id)
        else postDao.getAuthor(id) ?: fetchAuthorFromApi(id)

    private suspend fun fetchAuthorFromApi(id: Int): DataAuthor =
        postApi.fetchAuthor(id).also { author ->
            updateDatabase(author)
        }

    private suspend fun updateDatabase(author: DataAuthor) {
        postDao.insertAuthor(author)
        postDao.updateLastSaved(
            LastSaved(
                what = PostDatabase.AUTHORS_TABLE,
                time = system.currentMillis
            )
        )
    }
    // endregion author
}
