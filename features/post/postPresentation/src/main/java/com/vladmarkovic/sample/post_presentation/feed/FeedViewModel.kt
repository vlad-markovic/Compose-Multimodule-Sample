package com.vladmarkovic.sample.post_presentation.feed

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.vladmarkovic.sample.post_domain.PostRepository
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.shared_domain.DispatcherProvider
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val dispatchers: DispatcherProvider
): BriefActionViewModel() {

    private val _loading: MutableState<Boolean> = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _error: MutableState<Boolean> = mutableStateOf(false)
    val error: State<Boolean> = _error

    private val _posts: MutableState<List<Post>> = mutableStateOf(emptyList())
    val posts: State<List<Post>> = _posts

    init {
        refreshPosts(forceRefresh = false)
    }

    fun refreshPosts(forceRefresh: Boolean) {
        _error.value = false
        _loading.value = true

        viewModelScope.launch(dispatchers.io) {
            val (posts, error) = try {
                Pair(postRepository.fetchAllPosts(forceRefresh), null)
            } catch (e: Exception) {
                Timber.e(e, "Error fetching posts")
                Pair(null, true)
            }

            withContext(dispatchers.main) {
                _loading.value = false
                posts?.let { _posts.value = it }
                error?.let { _error.value = it }
            }
        }
    }

}