/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.feed

import androidx.lifecycle.viewModelScope
import com.vladmarkovic.sample.common.logging.Lumber
import com.vladmarkovic.sample.common.view.action.ActionViewModel
import com.vladmarkovic.sample.core.coroutines.collectIn
import com.vladmarkovic.sample.post_domain.PostRepository
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.shared_domain.connectivity.NetworkConnectivity
import com.vladmarkovic.sample.shared_domain.model.DataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val postRepository: PostRepository,
    connection: NetworkConnectivity
): ActionViewModel() {

    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val error: StateFlow<Boolean> = _error.asStateFlow()

    private val _posts: MutableStateFlow<List<Post>> = MutableStateFlow(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    init {
        connection.connectionState.collectIn(viewModelScope) { connected ->
            if (connected && _error.value) {
                refreshPosts(DataSource.REMOTE)
            }
        }

        postRepository.observePostsCache().collectIn(viewModelScope) { posts ->
            _posts.value = posts
        }

        refreshPosts()
    }

    fun refreshPosts(forceFetch: DataSource = DataSource.UNSPECIFIED) {
        viewModelScope.launch {
            _error.value = false
            _loading.value = true
            if (forceFetch == DataSource.CACHE) {
                _posts.value = emptyList()
            }

            val (posts, error) = try {
                Pair(postRepository.fetchAllPosts(forceFetch), null)
            } catch (exception: Exception) {
                Lumber.e(exception, "Error fetching posts")
                when (exception) {
                    is UnresolvedAddressException, is IOException -> {
                        val cachedPosts = postRepository.fetchAllPosts(DataSource.CACHE)
                        if (cachedPosts.isEmpty()) Pair(null, true)
                        else Pair(cachedPosts, null)
                    }
                    else -> {
                        throw exception
                    }
                }
            }

            _loading.value = false
            posts?.let { _posts.value = it }
            error?.let { _error.value = it }
        }
    }
}
