package com.vladmarkovic.sample.post_presentation

import com.vladmarkovic.sample.post_domain.PostRepository
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.shared_domain.model.DataSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakePostRepository : PostRepository {

    companion object{
        const val FAKE_FETCH_DELAY = 3L
    }

    private var initialPosts = fakeInitialPosts.toMutableList()

    override suspend fun fetchAllPosts(forceFetch: DataSource): List<Post> {
        delay(FAKE_FETCH_DELAY - 1)
        return if (forceFetch == DataSource.REMOTE) fakeRefreshedPosts else initialPosts
    }

    override fun observePostsCache(): Flow<List<Post>> = flowOf(initialPosts)

    override suspend fun deletePost(post: Post) {
        initialPosts.remove(post)
    }
}
