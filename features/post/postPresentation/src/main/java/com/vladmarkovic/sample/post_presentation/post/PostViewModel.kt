/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.post

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.vladmarkovic.sample.common.logging.Lumber
import com.vladmarkovic.sample.common.mv.action.ActionViewModel
import com.vladmarkovic.sample.common.mv.action.navigate
import com.vladmarkovic.sample.core.coroutines.collectIn
import com.vladmarkovic.sample.post_domain.AuthorRepository
import com.vladmarkovic.sample.post_domain.PostRepository
import com.vladmarkovic.sample.post_domain.model.Author
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.model.PostArg
import com.vladmarkovic.sample.shared_domain.connectivity.NetworkConnectivity
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.CommonNavigationAction.Back
import com.vladmarkovic.sample.shared_presentation.screen.ScreenArgNames
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val authorRepository: AuthorRepository,
    private val state: SavedStateHandle,
    connection: NetworkConnectivity
) : ActionViewModel() {

    val post: Post = Json.decodeFromString<PostArg>(state.get<String>(ScreenArgNames.POST.name)!!)

    private val _authorResult: MutableStateFlow<Result<Author>?> = MutableStateFlow(null)

    /** null value (data unavailable) means loading */
    val authorResult: StateFlow<Result<Author>?> = _authorResult.asStateFlow()

    init {
        connection.connectionState.collectIn(viewModelScope) { connected ->
            if (connected && _authorResult.value?.isFailure != false) {
                getDetails()
            }
        }

        getDetails()
    }

    fun getDetails() {
        viewModelScope.launch {
            _authorResult.value = null

            val authorResult = try {
                val author = authorRepository.fetchAuthor(post.userId)
                Result.success(author)
            } catch (e: Exception) {
                Lumber.e(e, "Error fetching author")
                Result.failure(e)
            }

            _authorResult.value = authorResult
        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch {
            postRepository.deletePost(post)

            navigate(Back)
        }
    }

    override fun onCleared() {
        super.onCleared()

        state[ScreenArgNames.POST.name] = null
    }
}
