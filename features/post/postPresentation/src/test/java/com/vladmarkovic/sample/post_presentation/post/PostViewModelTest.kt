package com.vladmarkovic.sample.post_presentation.post

import com.vladmarkovic.sample.post_domain.AuthorRepository
import com.vladmarkovic.sample.post_domain.model.Author
import com.vladmarkovic.sample.post_presentation.fakeAuthor
import com.vladmarkovic.sample.post_presentation.fakeAuthorSuccessResult
import com.vladmarkovic.sample.post_presentation.fakePost
import com.vladmarkovic.sample.shared_test.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.IOException
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class PostViewModelTest {

    companion object {
        private val dispatchers = TestDispatcherProvider()

        @JvmField
        @RegisterExtension
        @Suppress("Unused")
        val testSetupExtension: CustomizableAllTestSetupExtension =
            CustomizableAllTestSetupExtension()
                .setupCoroutines(dispatchers.main)
                .setupLiveData()

        private const val FAKE_FETCH_DELAY = 2L
    }

    private lateinit var authorRepository : AuthorRepository

    private lateinit var viewModel: PostViewModel

    @BeforeEach
    fun setup() {
        authorRepository = FakeAuthorRepository()

        viewModel = PostViewModel(authorRepository, dispatchers)
    }

    @Test
    @DisplayName(
        "Given loading author for a post, " +
                "It shows post info and loading, and shows author info after fetched"
    )
    fun testInitialStateAndSuccessAuthorResult() {
        viewModel.post.assertValueEquals(null)

        viewModel.getDetails(fakePost)

        viewModel.post.assertValueEquals(fakePost)
        viewModel.authorResult.assertValueEquals(null)

        testSetupExtension.testDispatcher!!.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.authorResult.assertValueEquals(fakeAuthorSuccessResult)
    }

    @Test
    @DisplayName(
        "Given loading author for a post, " +
                "When an error occurs, It shows an error message"
    )
    fun testFailureAuthorResult() {
        val exception = IOException()
        authorRepository = FakeAuthorRepository(exception)
        val viewModel = PostViewModel(authorRepository, dispatchers)

        viewModel.getDetails(fakePost)

        testSetupExtension.testDispatcher!!.advanceTimeBy(FAKE_FETCH_DELAY)

        assertEquals(exception, viewModel.authorResult.value!!.exceptionOrNull()!!)
    }

    private class FakeAuthorRepository(private val t: Throwable? = null) : AuthorRepository {
        override suspend fun fetchAuthor(id: Int): Author {
            t?.let { throw it } ?: delay(FAKE_FETCH_DELAY)
            return fakeAuthor
        }
    }
}
