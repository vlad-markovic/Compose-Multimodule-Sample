package com.vladmarkovic.sample.post_presentation.post

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladmarkovic.sample.post_domain.AuthorRepository
import com.vladmarkovic.sample.post_domain.model.Author
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.shared_domain.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val authorRepository: AuthorRepository,
    private val dispatchers: DispatcherProvider
): ViewModel() {

    private val _post: MutableState<Post?> = mutableStateOf(null)
    /** null value (data unavailable) means loading */
    val post: State<Post?> = _post

    private val _authorResult: MutableState<Result<Author>?> = mutableStateOf(null)
    /** null value (data unavailable) means loading */
    val authorResult: State<Result<Author>?> = _authorResult

    fun getDetails(post: Post) {
        _post.value = post

        viewModelScope.launch(dispatchers.io) {
            val authorResult = try {
                val author = authorRepository.fetchAuthor(post.userId)
                Result.success(author)
            } catch (e: Exception) {
                Timber.e(e, "Error fetching author")
                Result.failure(e)
            }

            withContext(dispatchers.main) {
                _authorResult.value = authorResult
            }
        }
    }
}
