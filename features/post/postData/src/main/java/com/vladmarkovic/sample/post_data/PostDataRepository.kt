package com.vladmarkovic.sample.post_data

import androidx.annotation.VisibleForTesting
import com.vladmarkovic.sample.post_data.PostDatabase.Companion.AUTHORS_TABLE
import com.vladmarkovic.sample.post_data.PostDatabase.Companion.POSTS_TABLE
import com.vladmarkovic.sample.post_data.model.DataAuthor
import com.vladmarkovic.sample.post_data.model.DataPost
import com.vladmarkovic.sample.post_domain.AuthorRepository
import com.vladmarkovic.sample.post_domain.PostRepository
import com.vladmarkovic.sample.post_domain.model.Author
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.shared_data.model.LastSaved
import com.vladmarkovic.sample.shared_domain.AppSystem
import com.vladmarkovic.sample.shared_domain.model.DataSource
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
    override suspend fun fetchAllPosts(forceFetch: DataSource): List<Post> =
        when (forceFetch) {
            DataSource.REMOTE -> fetchAllPostsFromRemote()
            DataSource.CACHE -> fetchAllPostsFromCache()
            DataSource.UNSPECIFIED -> {
                if (cacheExpired(POSTS_TABLE)) fetchAllPostsFromRemote()
                else fetchAllPostsFromCache()
            }
        }

    @VisibleForTesting
    suspend fun cacheExpired(tableName: String): Boolean =
        lastSaved(tableName) <= system.currentMillis - CACHE_EXPIRY_MILLIS

    private suspend fun lastSaved(tableName: String): Long =
        postDao.getLastSaved(tableName)?.time ?: 0L

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
    // endregion posts

    // region author
    override suspend fun fetchAuthor(id: Int): Author =
        if (cacheExpired(AUTHORS_TABLE)) fetchAuthorFromApi(id)
        else postDao.getAuthor(id) ?: fetchAuthorFromApi(id)

    private suspend fun fetchAuthorFromApi(id: Int): DataAuthor =
        postApi.fetchAuthor(id).also { author ->
            updateDatabase(author)
        }

    private suspend fun updateDatabase(author: DataAuthor) {
        postDao.insertAuthor(author)
        postDao.updateLastSaved(
            LastSaved(
                what = AUTHORS_TABLE,
                time = system.currentMillis
            )
        )
    }
    // endregion author
}
