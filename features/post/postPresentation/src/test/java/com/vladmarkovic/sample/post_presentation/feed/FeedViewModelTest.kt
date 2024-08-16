/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.feed

import androidx.lifecycle.SavedStateHandle
import com.vladmarkovic.sample.post_domain.PostRepository
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.FakePostRepository
import com.vladmarkovic.sample.post_presentation.FakePostRepository.Companion.FAKE_FETCH_DELAY
import com.vladmarkovic.sample.post_presentation.fakeInitialPosts
import com.vladmarkovic.sample.post_presentation.fakePost
import com.vladmarkovic.sample.post_presentation.fakeRefreshedPosts
import com.vladmarkovic.sample.shared_domain.model.DataSource
import com.vladmarkovic.sample.shared_presentation.screen.ScreenArgNames
import com.vladmarkovic.sample.shared_test.CustomizableAllTestSetupExtension
import com.vladmarkovic.sample.shared_test.TestNetworkConnectivity
import com.vladmarkovic.sample.shared_test.assertValueEquals
import com.vladmarkovic.sample.shared_test.setupCoroutines
import com.vladmarkovic.sample.shared_test.setupLogger
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class FeedViewModelTest {

    companion object {
        private val testDispatcher = StandardTestDispatcher()

        @JvmField
        @RegisterExtension
        @Suppress("Unused")
        val testSetupExtension: CustomizableAllTestSetupExtension =
            CustomizableAllTestSetupExtension()
                .setupCoroutines(testDispatcher)
                .setupLogger()
    }

    private lateinit var fakePostRepository: PostRepository
    private lateinit var mockSavedStateHandle: SavedStateHandle
    private lateinit var testNetworkConnectivity: TestNetworkConnectivity

    private lateinit var viewModel: FeedViewModel

    @BeforeEach
    fun setup() {
        fakePostRepository = FakePostRepository()
        mockSavedStateHandle = mockk()
        testNetworkConnectivity = TestNetworkConnectivity()

        every { mockSavedStateHandle.set<Post>(ScreenArgNames.POST.name, fakePost) }.answers { }

        viewModel = FeedViewModel(fakePostRepository, testNetworkConnectivity)
    }

    @Test
    @DisplayName(
        "Given landing on Feed screen, " +
                "It shows loading while fetching and shows posts after fetched"
    )
    fun testInitialState() {
        viewModel.state.assertValueEquals(FeedState.Loading)

        testDispatcher.scheduler.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.state.assertValueEquals(FeedState.Posts(fakeInitialPosts))
    }

    @Test
    @DisplayName(
        "Given refreshing posts, " +
                "It shows loading while fetching and shows refreshed posts after fetched"
    )
    fun testRefreshingPosts() {
        testDispatcher.scheduler.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.state.assertValueEquals(FeedState.Posts(fakeInitialPosts))

        viewModel.onEvent(FeedEvent.OnRefreshPosts(DataSource.REMOTE))

        testDispatcher.scheduler.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.state.assertValueEquals(FeedState.Posts(fakeRefreshedPosts))
    }

    @Test
    @DisplayName(
        "Given re-fetching saved posts, " +
                "It shows loading while fetching and shows saved posts after fetched"
    )
    fun testReFetchingPosts() {
        testDispatcher.scheduler.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.state.assertValueEquals(FeedState.Posts(fakeInitialPosts))

        viewModel.onEvent(FeedEvent.OnRefreshPosts(DataSource.CACHE))

        testDispatcher.scheduler.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.state.assertValueEquals(FeedState.Posts(fakeInitialPosts))
    }

    @Test
    @DisplayName("Given loading posts, When an error occurs, It shows an error message")
    fun testFailureToFetchPosts() {
        val mockPostRepository = mockk<PostRepository>()
        coEvery { mockPostRepository.fetchAllPosts(DataSource.UNSPECIFIED) }.throws(IOException())
        coEvery { mockPostRepository.fetchAllPosts(DataSource.REMOTE) }.throws(IOException())
        coEvery { mockPostRepository.fetchAllPosts(DataSource.CACHE) }.returns(emptyList())
        coEvery { mockPostRepository.observePostsCache() }.returns(flowOf(emptyList()))

        val viewModel = FeedViewModel(mockPostRepository, testNetworkConnectivity)

        viewModel.onEvent(FeedEvent.OnRefreshPosts(DataSource.REMOTE))

        testDispatcher.scheduler.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.state.assertValueEquals(FeedState.Error)
    }

    @Test
    @DisplayName(
        "Given internet is disconnected and posts are not fetched, " +
                "When internet reconnects, It tries to fetch posts"
    )
    fun testOnErrorAndAutoReloadOnConnected() {
        val mockPostRepository = mockk<PostRepository>()
        coEvery { mockPostRepository.fetchAllPosts() }.throws(IOException())
        coEvery { mockPostRepository.fetchAllPosts(DataSource.REMOTE) }.throws(IOException())
        coEvery { mockPostRepository.fetchAllPosts(DataSource.CACHE) }.returns(emptyList())
        every { mockPostRepository.observePostsCache() }.returns(flowOf(emptyList()))

        FeedViewModel(mockPostRepository, testNetworkConnectivity)

        assertFalse(testNetworkConnectivity.isConnected)

        testDispatcher.scheduler.runCurrent()

        // One initial call
        coVerify(exactly = 1) { mockPostRepository.fetchAllPosts() }
        // First fallback to cache on error
        coVerify(exactly = 1) { mockPostRepository.fetchAllPosts(DataSource.CACHE) }

        testNetworkConnectivity.state.value = true
        assertTrue(testNetworkConnectivity.isConnected)

        testDispatcher.scheduler.advanceTimeBy(FAKE_FETCH_DELAY)

        // Another call after connected
        coVerify(exactly = 1) { mockPostRepository.fetchAllPosts(DataSource.REMOTE) }
        // Second fallback to cache on error
        coVerify(exactly = 2) { mockPostRepository.fetchAllPosts(DataSource.CACHE) }
    }
}
