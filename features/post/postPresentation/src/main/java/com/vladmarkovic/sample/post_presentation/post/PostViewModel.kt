/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.post

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.vladmarkovic.sample.post_domain.AuthorRepository
import com.vladmarkovic.sample.post_domain.PostRepository
import com.vladmarkovic.sample.post_domain.model.Author
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.model.PostArg
import com.vladmarkovic.sample.shared_domain.DispatcherProvider
import com.vladmarkovic.sample.shared_domain.connectivity.NetworkConnectivity
import com.vladmarkovic.sample.shared_domain.log.Lumber
import com.vladmarkovic.sample.shared_domain.util.doOnMainOnConnectionChange
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.briefaction.navigate
import com.vladmarkovic.sample.shared_presentation.navigation.CommonNavigationAction.Back
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.ArgKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val authorRepository: AuthorRepository,
    private val dispatchers: DispatcherProvider,
    private val state: SavedStateHandle,
    connection: NetworkConnectivity
) : BriefActionViewModel() {

    val post: Post = Json.decodeFromString<PostArg>(state.get<String>(ArgKeys.POST.name)!!)

    private val _authorResult: MutableStateFlow<Result<Author>?> = MutableStateFlow(null)

    /** null value (data unavailable) means loading */
    val authorResult: StateFlow<Result<Author>?> = _authorResult.asStateFlow()

    init {
        connection.doOnMainOnConnectionChange(viewModelScope, dispatchers) { connected ->
            if (connected && _authorResult.value?.isFailure != false) getDetails()
        }

        getDetails()
    }

    fun getDetails() {
        _authorResult.value = null

        viewModelScope.launch(dispatchers.io) {
            val authorResult = try {
                val author = authorRepository.fetchAuthor(post.userId)
                Result.success(author)
            } catch (e: Exception) {
                Lumber.e(e.stackTraceToString(), "Error fetching author")
                Result.failure(e)
            }

            withContext(dispatchers.main) {
                _authorResult.value = authorResult
            }
        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch(dispatchers.io) {
            postRepository.deletePost(post)

            withContext(dispatchers.main) {
                navigate(Back)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        state[ArgKeys.POST.name] = null
    }
}
