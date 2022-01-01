package com.vladmarkovic.sample.post_presentation.feed

import androidx.lifecycle.SavedStateHandle
import com.vladmarkovic.sample.post_domain.PostRepository
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.fakeInitialPosts
import com.vladmarkovic.sample.post_presentation.fakePost
import com.vladmarkovic.sample.post_presentation.fakeRefreshedPosts
import com.vladmarkovic.sample.post_presentation.post.PostViewModel
import com.vladmarkovic.sample.shared_test.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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

    private lateinit var fakePostRepository: PostRepository
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var viewModel: FeedViewModel

    @BeforeEach
    fun setup() {
        fakePostRepository = FakePostRepository()
        savedStateHandle = mockk()

        every { savedStateHandle.set<Post>(PostViewModel.POST_ARG_KEY, fakePost) }.answers {  }

        viewModel = FeedViewModel(fakePostRepository, dispatchers, savedStateHandle)
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
        testSetupExtension.testDispatcher!!.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.refreshPosts(forceRefresh = true)

        viewModel.loading.assertValueEquals(true)
        viewModel.error.assertValueEquals(false)
        runBlockingTest { assertEquals(fakeInitialPosts, viewModel.posts.value) }

        testSetupExtension.testDispatcher!!.advanceTimeBy(FAKE_FETCH_DELAY)

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
        testSetupExtension.testDispatcher!!.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.refreshPosts(forceRefresh = false)

        viewModel.loading.assertValueEquals(true)
        viewModel.error.assertValueEquals(false)
        runBlockingTest { assertEquals(fakeInitialPosts, viewModel.posts.value) }

        testSetupExtension.testDispatcher!!.advanceTimeBy(FAKE_FETCH_DELAY)

        viewModel.loading.assertValueEquals(false)
        viewModel.error.assertValueEquals(false)
        runBlockingTest { assertEquals(fakeInitialPosts, viewModel.posts.value) }
    }

    @Test
    @DisplayName("Given loading posts, When an error occurs, It shows an error message")
    fun testFailureToFetchPosts() {
        fakePostRepository = mockk()
        val viewModel = FeedViewModel(fakePostRepository, dispatchers, savedStateHandle)

        viewModel.refreshPosts(forceRefresh = true)

        testSetupExtension.testDispatcher!!.advanceTimeBy(FAKE_FETCH_DELAY)

        assertTrue(viewModel.error.value)
    }

    @Test
    @DisplayName(
        "Given on a post on feed screen, " +
            "When navigating to post details for a post, " +
            "It saves the post to SavedStateHandle, " +
            "and navigates to that post details screen"
    )
    fun testNavigatingToPostDetails() {
        val spyViewModel = spyk(viewModel)
        spyViewModel.navigateToPostDetails(fakePost)

        verify { savedStateHandle.set(PostViewModel.POST_ARG_KEY, fakePost) }
        verify { spyViewModel.navigate(ToPostScreen(fakePost)) }
    }


    private class FakePostRepository : PostRepository {
        override suspend fun fetchAllPosts(forceRefresh: Boolean): List<Post> {
            delay(FAKE_FETCH_DELAY)
            return if (forceRefresh) fakeRefreshedPosts else fakeInitialPosts
        }
    }
}
