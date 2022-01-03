/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.feed

import androidx.lifecycle.SavedStateHandle
import com.vladmarkovic.sample.post_domain.PostRepository
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.fakeInitialPosts
import com.vladmarkovic.sample.post_presentation.fakePost
import com.vladmarkovic.sample.post_presentation.fakeRefreshedPosts
import com.vladmarkovic.sample.shared_domain.model.DataSource
import com.vladmarkovic.sample.shared_presentation.screen.PostsScreen.ArgKeys
import com.vladmarkovic.sample.shared_test.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
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

    private lateinit var fakePostRepository: PostRepository
    private lateinit var mockSavedStateHandle: SavedStateHandle
    private lateinit var testNetworkConnectivity: TestNetworkConnectivity

    private lateinit var viewModel: FeedViewModel

    @BeforeEach
    fun setup() {
        fakePostRepository = FakePostRepository()
        mockSavedStateHandle = mockk()
        testNetworkConnectivity = TestNetworkConnectivity()

        every { mockSavedStateHandle.set<Post>(ArgKeys.POST.name, fakePost) }.answers { }

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
        runBlockingTest { assertEquals(0, viewModel.posts.value.count()) }

        testSetupExtension.testDispatcher!!.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.loading.assertValueEquals(false)
        viewModel.error.assertValueEquals(false)
        runBlockingTest { assertEquals(fakeInitialPosts, viewModel.posts.value) }
    }

    @Test
    @DisplayName(
        "Given refreshing posts, " +
                "It shows loading while fetching and shows refreshed posts after fetched"
    )
    fun testRefreshingPosts() {
        testDispatcher.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.refreshPosts(DataSource.REMOTE)

        viewModel.loading.assertValueEquals(true)
        viewModel.error.assertValueEquals(false)
        runBlockingTest { assertEquals(fakeInitialPosts, viewModel.posts.value) }

        testDispatcher.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.loading.assertValueEquals(false)
        viewModel.error.assertValueEquals(false)
        runBlockingTest { assertEquals(fakeRefreshedPosts, viewModel.posts.value) }
    }

    @Test
    @DisplayName(
        "Given re-fetching saved posts, " +
                "It shows loading while fetching and shows saved posts after fetched"
    )
    fun testReFetchingPosts() {
        testDispatcher.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.refreshPosts(DataSource.CACHE)

        viewModel.loading.assertValueEquals(true)
        viewModel.error.assertValueEquals(false)
        runBlockingTest { assertEquals(emptyList(), viewModel.posts.value) }

        testDispatcher.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.loading.assertValueEquals(false)
        viewModel.error.assertValueEquals(false)
        runBlockingTest { assertEquals(fakeInitialPosts, viewModel.posts.value) }
    }

    @Test
    @DisplayName("Given loading posts, When an error occurs, It shows an error message")
    fun testFailureToFetchPosts() {
        fakePostRepository = mockk()
        coEvery { fakePostRepository.fetchAllPosts(DataSource.UNSPECIFIED) }.throws(IOException())
        coEvery { fakePostRepository.fetchAllPosts(DataSource.REMOTE) }.throws(IOException())
        coEvery { fakePostRepository.fetchAllPosts(DataSource.CACHE) }.returns(emptyList())

        val viewModel = FeedViewModel(
            fakePostRepository,
            testDispatchers,
            testNetworkConnectivity
        )

        viewModel.refreshPosts(DataSource.REMOTE)

        testDispatcher.advanceTimeBy(FAKE_FETCH_DELAY)

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

        FeedViewModel(
            mockPostRepository,
            testDispatchers,
            testNetworkConnectivity
        )

        assertFalse(testNetworkConnectivity.isConnected)
        // One initial call
        coVerify(exactly = 1) { mockPostRepository.fetchAllPosts()  }
        // First fallback to cache on error
        coVerify(exactly = 1) { mockPostRepository.fetchAllPosts(DataSource.CACHE)  }

        testNetworkConnectivity.state.value = true

        testDispatcher.advanceTimeBy(FAKE_FETCH_DELAY)

        // Another call after connected
        coVerify(exactly = 1) { mockPostRepository.fetchAllPosts(DataSource.REMOTE)  }
        // Second fallback to cache on error
        coVerify(exactly = 2) { mockPostRepository.fetchAllPosts(DataSource.CACHE)  }
    }

    class FakePostRepository : PostRepository {
        private var initialPosts = fakeInitialPosts.toMutableList()

        override suspend fun fetchAllPosts(forceFetch: DataSource): List<Post> {
            delay(FAKE_FETCH_DELAY)
            return if (forceFetch == DataSource.REMOTE) fakeRefreshedPosts else initialPosts
        }

        override fun observePostsCache(): Flow<List<Post>> = flowOf(emptyList())

        override suspend fun deletePost(post: Post) {
            initialPosts.remove(post)
        }
    }
}
