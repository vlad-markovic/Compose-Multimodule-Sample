package com.vladmarkovic.sample.post_presentation.feed

import com.vladmarkovic.sample.post_domain.PostRepository
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.fakeInitialFeedPostItems
import com.vladmarkovic.sample.post_presentation.fakeInitialPosts
import com.vladmarkovic.sample.post_presentation.fakeRefreshedFeedPostItems
import com.vladmarkovic.sample.post_presentation.fakeRefreshedPosts
import com.vladmarkovic.sample.shared_test.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class FeedViewModelTest {

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

    private val fakePostRepository = FakePostRepository()

    private lateinit var viewModel: FeedViewModel

    private var fakeFetchedPosts = emptyList<Post>()

    @BeforeEach
    fun setup() {
        fakeFetchedPosts = fakeInitialPosts

        viewModel = FeedViewModel(fakePostRepository, dispatchers)
    }

    @Test
    @DisplayName(
        "Given landing on Feed screen, " +
                "It shows loading while fetching and shows posts after fetched"
    )
    fun testInitialState() {
        viewModel.loading.assertValueEquals(true)
        viewModel.error.assertValueEquals(null)
        runBlockingTest { assertEquals(0, viewModel.posts.first().count()) }

        testSetupExtension.testDispatcher!!.advanceTimeBy(FAKE_FETCH_DELAY * 2)

        viewModel.loading.assertValueEquals(false)
        viewModel.error.assertValueEquals(null)
        runBlockingTest { assertEquals(fakeInitialFeedPostItems, viewModel.posts.first()) }
    }

    @Test
    @DisplayName(
        "Given refreshing posts, " +
                "It shows loading while fetching and shows refreshed posts after fetched"
    )
    fun testRefreshingPosts() {
        testSetupExtension.testDispatcher!!.advanceTimeBy(FAKE_FETCH_DELAY)
        fakeFetchedPosts = fakeRefreshedPosts

        viewModel.refreshPosts()

        viewModel.loading.assertValueEquals(true)
        viewModel.error.assertValueEquals(null)
        runBlockingTest { assertEquals(fakeInitialFeedPostItems, viewModel.posts.first()) }

        testSetupExtension.testDispatcher!!.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.loading.assertValueEquals(false)
        viewModel.error.assertValueEquals(null)
        runBlockingTest { assertEquals(fakeRefreshedFeedPostItems, viewModel.posts.first()) }
    }

    private inner class FakePostRepository : PostRepository {
        override suspend fun fetchPosts(): List<Post> {
            delay(FAKE_FETCH_DELAY)
            return fakeFetchedPosts
        }
    }
}
