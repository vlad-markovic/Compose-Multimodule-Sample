/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.post

import androidx.lifecycle.SavedStateHandle
import com.vladmarkovic.sample.post_presentation.*
import com.vladmarkovic.sample.post_presentation.FakeAuthorRepository.Companion.FAKE_FETCH_DELAY
import com.vladmarkovic.sample.post_domain.AuthorRepository
import com.vladmarkovic.sample.post_domain.PostRepository
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.shared_presentation.briefaction.navigate
import com.vladmarkovic.sample.shared_presentation.navigation.CommonNavigationAction.Back
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.ArgKeys
import com.vladmarkovic.sample.shared_test.*
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
        private val testDispatcher = UnconfinedTestDispatcher()
        private val testDispatchers = TestDispatcherProvider(testDispatcher)

        @JvmField
        @RegisterExtension
        @Suppress("Unused")
        val testSetupExtension: CustomizableAllTestSetupExtension =
            CustomizableAllTestSetupExtension()
                .setupCoroutines(testDispatcher)
                .setupLiveData()
                .setupLogger()
    }

    private lateinit var fakePostRepository: FakePostRepository
    private lateinit var fakeAuthorRepository: FakeAuthorRepository
    private lateinit var mockSavedStateHandle: SavedStateHandle
    private lateinit var testNetworkConnectivity: TestNetworkConnectivity

    private lateinit var viewModel: PostViewModel

    @BeforeEach
    fun setup() {
        fakePostRepository = FakePostRepository()
        fakeAuthorRepository = FakeAuthorRepository()
        mockSavedStateHandle = mockk()
        testNetworkConnectivity = TestNetworkConnectivity()

        val mockJsonString = "{\"id\":$fakePostId,\"userId\":$fakePostUserId,\"title\":\"" +
                "$fakePostTitle\",\"content\":\"$fakePostContent\"}"
        every { mockSavedStateHandle.get<String>(ArgKeys.POST.name) }.returns(mockJsonString)

        viewModel = PostViewModel(
            fakePostRepository,
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

        testDispatcher.scheduler.advanceTimeBy(FAKE_FETCH_DELAY)

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
            fakePostRepository,
            fakeAuthorRepository,
            testDispatchers,
            mockSavedStateHandle,
            testNetworkConnectivity
        )

        testDispatcher.scheduler.advanceTimeBy(FAKE_FETCH_DELAY)

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
            fakePostRepository,
            mockAuthorRepository,
            testDispatchers,
            mockSavedStateHandle,
            testNetworkConnectivity
        )

        assertFalse(testNetworkConnectivity.isConnected)
        // One initial call
        coVerify(exactly = 1) { mockAuthorRepository.fetchAuthor(any()) }

        testNetworkConnectivity.state.value = true

        testDispatcher.scheduler.advanceTimeBy(FAKE_FETCH_DELAY)
        // Another call after connected
        coVerify(exactly = 2) { mockAuthorRepository.fetchAuthor(any()) }
    }

    @Test
    @DisplayName("Given delete post, It removes post from cache, And navigates back")
    fun testDeletingPost() {
        val mockPostRepository = mockk<PostRepository>()
        coEvery { mockPostRepository.deletePost(any()) }.answers {  }

        val viewModel = PostViewModel(
            mockPostRepository,
            fakeAuthorRepository,
            testDispatchers,
            mockSavedStateHandle,
            testNetworkConnectivity
        )

        testDispatcher.scheduler.advanceTimeBy(FAKE_FETCH_DELAY)

        val post = fakeInitialPosts.first()

        val spyViewModel = spyk(viewModel)

        spyViewModel.deletePost(post)

        val postSlot = slot<Post>()
        coVerify(exactly = 1) { mockPostRepository.deletePost(capture(postSlot)) }
        assertEquals(post, postSlot.captured)
        coVerify(exactly = 1) { spyViewModel.navigate(Back) }
    }
}
