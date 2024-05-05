/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.feed

import androidx.lifecycle.SavedStateHandle
import com.vladmarkovic.sample.post_presentation.FakePostRepository
import com.vladmarkovic.sample.post_presentation.FakePostRepository.Companion.FAKE_FETCH_DELAY
import com.vladmarkovic.sample.post_presentation.fakeInitialPosts
import com.vladmarkovic.sample.post_presentation.fakePost
import com.vladmarkovic.sample.post_presentation.fakeRefreshedPosts
import com.vladmarkovic.sample.post_domain.PostRepository
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.shared_domain.model.DataSource
import com.vladmarkovic.sample.shared_presentation.screen.ScreenArgKeys
import com.vladmarkovic.sample.shared_test.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
        private val testDispatcher = UnconfinedTestDispatcher()
        private val testDispatchers = TestDispatcherProvider(testDispatcher)

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

        every { mockSavedStateHandle.set<Post>(ScreenArgKeys.POST.name, fakePost) }.answers { }

        viewModel = FeedViewModel(
            fakePostRepository,
            testDispatchers,
            testNetworkConnectivity
        )
    }

    @Test
    @DisplayName(
        "Given landing on Feed screen, " +
                "It shows loading while fetching and shows posts after fetched"
    )
    fun testInitialState() {
        viewModel.loading.assertValueEquals(true)
        viewModel.error.assertValueEquals(false)
        runTest { assertEquals(fakeInitialPosts.size, viewModel.posts.value.count()) }

        testDispatcher.scheduler.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.loading.assertValueEquals(false)
        viewModel.error.assertValueEquals(false)
        runTest { assertEquals(fakeInitialPosts, viewModel.posts.value) }
    }

    @Test
    @DisplayName(
        "Given refreshing posts, " +
                "It shows loading while fetching and shows refreshed posts after fetched"
    )
    fun testRefreshingPosts() {
        testDispatcher.scheduler.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.refreshPosts(DataSource.REMOTE)

        viewModel.loading.assertValueEquals(true)
        viewModel.error.assertValueEquals(false)
        runTest { assertEquals(fakeInitialPosts, viewModel.posts.value) }

        testDispatcher.scheduler.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.loading.assertValueEquals(false)
        viewModel.error.assertValueEquals(false)
        runTest { assertEquals(fakeRefreshedPosts, viewModel.posts.value) }
    }

    @Test
    @DisplayName(
        "Given re-fetching saved posts, " +
                "It shows loading while fetching and shows saved posts after fetched"
    )
    fun testReFetchingPosts() {
        testDispatcher.scheduler.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.refreshPosts(DataSource.CACHE)

        viewModel.loading.assertValueEquals(true)
        viewModel.error.assertValueEquals(false)
        runTest { assertEquals(emptyList(), viewModel.posts.value) }

        testDispatcher.scheduler.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.loading.assertValueEquals(false)
        viewModel.error.assertValueEquals(false)
        runTest { assertEquals(fakeInitialPosts, viewModel.posts.value) }
    }

    @Test
    @DisplayName("Given loading posts, When an error occurs, It shows an error message")
    fun testFailureToFetchPosts() {
        val mockPostRepository = mockk<PostRepository>()
        coEvery { mockPostRepository.fetchAllPosts(DataSource.UNSPECIFIED) }.throws(IOException())
        coEvery { mockPostRepository.fetchAllPosts(DataSource.REMOTE) }.throws(IOException())
        coEvery { mockPostRepository.fetchAllPosts(DataSource.CACHE) }.returns(emptyList())
        coEvery { mockPostRepository.observePostsCache() }.returns(flowOf(emptyList()))

        val viewModel = FeedViewModel(
            mockPostRepository,
            testDispatchers,
            testNetworkConnectivity
        )

        viewModel.refreshPosts(DataSource.REMOTE)

        testDispatcher.scheduler.advanceTimeBy(FAKE_FETCH_DELAY)

        assertTrue(viewModel.error.value)
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

        FeedViewModel(
            mockPostRepository,
            testDispatchers,
            testNetworkConnectivity
        )

        assertFalse(testNetworkConnectivity.isConnected)
        // One initial call
        coVerify(exactly = 1) { mockPostRepository.fetchAllPosts() }
        // First fallback to cache on error
        coVerify(exactly = 1) { mockPostRepository.fetchAllPosts(DataSource.CACHE) }

        testNetworkConnectivity.state.value = true

        testDispatcher.scheduler.advanceTimeBy(FAKE_FETCH_DELAY)

        // Another call after connected
        coVerify(exactly = 1) { mockPostRepository.fetchAllPosts(DataSource.REMOTE) }
        // Second fallback to cache on error
        coVerify(exactly = 2) { mockPostRepository.fetchAllPosts(DataSource.CACHE) }
    }
}
