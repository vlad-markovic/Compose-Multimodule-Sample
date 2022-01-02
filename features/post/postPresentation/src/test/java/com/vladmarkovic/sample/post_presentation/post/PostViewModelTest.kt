package com.vladmarkovic.sample.post_presentation.post

import androidx.lifecycle.SavedStateHandle
import com.vladmarkovic.sample.post_domain.AuthorRepository
import com.vladmarkovic.sample.post_domain.model.Author
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.fakeAuthor
import com.vladmarkovic.sample.post_presentation.fakeAuthorSuccessResult
import com.vladmarkovic.sample.post_presentation.fakePost
import com.vladmarkovic.sample.shared_test.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@ExperimentalCoroutinesApi
class PostViewModelTest {

    companion object {
        private val testDispatcher = TestCoroutineDispatcher()
        private val testDispatchers = TestDispatcherProvider(testDispatcher)

        @JvmField
        @RegisterExtension
        @Suppress("Unused")
        val testSetupExtension: CustomizableAllTestSetupExtension =
            CustomizableAllTestSetupExtension()
                .setupCoroutines(testDispatcher)
                .setupLiveData()
                .setupLogger()

        private const val FAKE_FETCH_DELAY = 2L
    }

    private lateinit var fakeAuthorRepository: FakeAuthorRepository
    private lateinit var mockSavedStateHandle: SavedStateHandle
    private lateinit var testNetworkConnectivity: TestNetworkConnectivity

    private lateinit var viewModel: PostViewModel

    @BeforeEach
    fun setup() {
        fakeAuthorRepository = FakeAuthorRepository()
        mockSavedStateHandle = mockk()
        testNetworkConnectivity = TestNetworkConnectivity()

        every { mockSavedStateHandle.get<Post>(PostViewModel.POST_ARG_KEY) }.returns(fakePost)

        viewModel = PostViewModel(
            fakeAuthorRepository,
            testDispatchers,
            mockSavedStateHandle,
            testNetworkConnectivity
        )
    }

    @Test
    @DisplayName(
        "Given loading author for a post, " +
                "It shows post info and loading, and shows author info after fetched"
    )
    fun testInitialStateAndSuccessAuthorResult() {
        assertEquals(fakePost, viewModel.post)
        viewModel.authorResult.assertValueEquals(null)

        testDispatcher.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.authorResult.assertValueEquals(fakeAuthorSuccessResult)
    }

    @Test
    @DisplayName(
        "Given loading author for a post, " +
                "When an error occurs, It shows an error message"
    )
    fun testFailureAuthorResult() {
        val exception = IOException()
        val fakeAuthorRepository = FakeAuthorRepository(exception)

        val viewModel = PostViewModel(
            fakeAuthorRepository,
            testDispatchers,
            mockSavedStateHandle,
            testNetworkConnectivity
        )

        testDispatcher.advanceTimeBy(FAKE_FETCH_DELAY)

        assertEquals(exception, viewModel.authorResult.value!!.exceptionOrNull()!!)
    }

    @Test
    @DisplayName(
        "Given internet is disconnected and author info is not fetched, " +
                "When internet reconnects, It tries to fetch author details"
    )
    fun testAutoReloadOnConnected() {
        val mockAuthorRepository = mockk<AuthorRepository>()
        coEvery { mockAuthorRepository.fetchAuthor(any()) }.throws(IOException())

        PostViewModel(
            mockAuthorRepository,
            testDispatchers,
            mockSavedStateHandle,
            testNetworkConnectivity
        )

        assertFalse(testNetworkConnectivity.isConnected)
        // One initial call
        coVerify(exactly = 1) { mockAuthorRepository.fetchAuthor(any()) }

        testNetworkConnectivity.state.value = true

        testDispatcher.advanceTimeBy(FAKE_FETCH_DELAY)
        // Another call after connected
        coVerify(exactly = 2) { mockAuthorRepository.fetchAuthor(any()) }
    }

    private class FakeAuthorRepository(private val t: Throwable? = null) : AuthorRepository {
        override suspend fun fetchAuthor(id: Int): Author {
            t?.let { throw it } ?: delay(FAKE_FETCH_DELAY)
            return fakeAuthor
        }
    }
}
