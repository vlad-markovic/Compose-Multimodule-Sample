package com.vladmarkovic.sample.post_presentation.feed

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.vladmarkovic.sample.post_domain.PostRepository
import com.vladmarkovic.sample.shared_domain.model.DataSource
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.post.PostViewModel.Companion.POST_ARG_KEY
import com.vladmarkovic.sample.shared_domain.DispatcherProvider
import com.vladmarkovic.sample.shared_domain.connectivity.NetworkConnectivity
import com.vladmarkovic.sample.shared_domain.util.doOnMainOnConnectionChange
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val dispatchers: DispatcherProvider,
    private val state: SavedStateHandle,
    connection: NetworkConnectivity
): BriefActionViewModel() {

    private val _loading: MutableState<Boolean> = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _error: MutableState<Boolean> = mutableStateOf(false)
    val error: State<Boolean> = _error

    private val _posts: MutableState<List<Post>> = mutableStateOf(emptyList())
    val posts: State<List<Post>> = _posts

    init {
        connection.doOnMainOnConnectionChange(viewModelScope, dispatchers) { connected ->
            if (connected && _error.value) {
                refreshPosts(DataSource.REMOTE)
            }
        }

        refreshPosts()
    }

    fun refreshPosts(forceFetch: DataSource = DataSource.UNSPECIFIED) {
        _error.value = false
        _loading.value = true

        viewModelScope.launch(dispatchers.io) {
            val (posts, error) = try {
                Pair(postRepository.fetchAllPosts(forceFetch), null)
            } catch (exception: Exception) {
                Timber.e(exception, "Error fetching posts")
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

            withContext(dispatchers.main) {
                _loading.value = false
                posts?.let { _posts.value = it }
                error?.let { _error.value = it }
            }
        }
    }

    fun navigateToPostDetails(post: Post) {
        state.set(POST_ARG_KEY, post)
        navigate(ToPostScreen(post))
    }
}