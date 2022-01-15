package com.vladmarkovic.sample.post_presentation.post

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.vladmarkovic.sample.post_domain.AuthorRepository
import com.vladmarkovic.sample.post_domain.model.Author
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.shared_domain.DispatcherProvider
import com.vladmarkovic.sample.shared_domain.connectivity.NetworkConnectivity
import com.vladmarkovic.sample.shared_domain.log.Lumber
import com.vladmarkovic.sample.shared_domain.util.doOnMainOnConnectionChange
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val authorRepository: AuthorRepository,
    private val dispatchers: DispatcherProvider,
    private val state: SavedStateHandle,
    connection: NetworkConnectivity
) : BriefActionViewModel() {

    companion object {
        const val POST_ARG_KEY = "POST_ARG_KEY"
    }

    val post: Post = state.get<Post>(POST_ARG_KEY)!!

    private val _authorResult: MutableState<Result<Author>?> = mutableStateOf(null)

    /** null value (data unavailable) means loading */
    val authorResult: State<Result<Author>?> = _authorResult

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
                Lumber.e(e, "Error fetching author")
                Result.failure(e)
            }

            withContext(dispatchers.main) {
                _authorResult.value = authorResult
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        state.set<Post>(POST_ARG_KEY, null)
    }
}
