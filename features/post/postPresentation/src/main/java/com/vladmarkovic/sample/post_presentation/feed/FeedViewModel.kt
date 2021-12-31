package com.vladmarkovic.sample.post_presentation.feed

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladmarkovic.sample.post_domain.PostRepository
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.feed.model.FeedPostItem
import com.vladmarkovic.sample.post_presentation.feed.model.items
import com.vladmarkovic.sample.shared_domain.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val dispatchers: DispatcherProvider
): ViewModel() {

    private val _loading: MutableState<Boolean> = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _error: MutableState<String?> = mutableStateOf(null)
    val error: State<String?> = _error

    private val _posts: MutableStateFlow<List<Post>> = MutableStateFlow(emptyList())
    val posts: Flow<List<FeedPostItem>> = _posts.map { it.items }

    init {
        refreshPosts(forceRefresh = false)
    }

    fun refreshPosts(forceRefresh: Boolean) {
        _error.value = null
        _loading.value = true

        viewModelScope.launch(dispatchers.io) {
            val (posts, error) = try {
                Pair(postRepository.fetchAllPosts(forceRefresh), null)
            } catch (e: Exception) {
                Timber.e(e, "Error fetching posts")
                Pair(null, "Failed to fetch posts")
            }

            withContext(dispatchers.main) {
                _loading.value = false
                posts?.let { _posts.value = it }
                error?.let { _error.value = it }
            }
        }
    }

}