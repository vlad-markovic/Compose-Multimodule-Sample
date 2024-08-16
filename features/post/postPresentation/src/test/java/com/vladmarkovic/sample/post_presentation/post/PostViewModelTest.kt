/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.post

import androidx.lifecycle.SavedStateHandle
import com.vladmarkovic.sample.post_domain.AuthorRepository
import com.vladmarkovic.sample.post_domain.PostRepository
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.FakeAuthorRepository
import com.vladmarkovic.sample.post_presentation.FakeAuthorRepository.Companion.FAKE_FETCH_DELAY
import com.vladmarkovic.sample.post_presentation.FakePostRepository
import com.vladmarkovic.sample.post_presentation.fakeAuthorSuccessResult
import com.vladmarkovic.sample.post_presentation.fakeInitialPosts
import com.vladmarkovic.sample.post_presentation.fakePost
import com.vladmarkovic.sample.post_presentation.fakePostContent
import com.vladmarkovic.sample.post_presentation.fakePostId
import com.vladmarkovic.sample.post_presentation.fakePostTitle
import com.vladmarkovic.sample.post_presentation.fakePostUserId
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.CommonNavigationAction.Back
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
import io.mockk.slot
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
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
        private val testDispatcher = StandardTestDispatcher()

        @JvmField
        @RegisterExtension
        @Suppress("Unused")
        val testSetupExtension: CustomizableAllTestSetupExtension =
            CustomizableAllTestSetupExtension()
                .setupCoroutines(testDispatcher)
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
        every { mockSavedStateHandle.get<String>(ScreenArgNames.POST.name) }.returns(mockJsonString)

        viewModel = PostViewModel(
            fakePostRepository,
            fakeAuthorRepository,
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
        fakeAuthorRepository = FakeAuthorRepository(exception)

        val viewModel = PostViewModel(
            fakePostRepository,
            fakeAuthorRepository,
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
            mockSavedStateHandle,
            testNetworkConnectivity
        )

        assertFalse(testNetworkConnectivity.isConnected)

        testDispatcher.scheduler.runCurrent()

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
            mockSavedStateHandle,
            testNetworkConnectivity
        )

        val spyViewModel = spyk(viewModel)

        val post = fakeInitialPosts.first()

        spyViewModel.deletePost(post)

        testDispatcher.scheduler.runCurrent()

        val postSlot = slot<Post>()
        coVerify(exactly = 1) { mockPostRepository.deletePost(capture(postSlot)) }
        assertEquals(post, postSlot.captured)
        coVerify(exactly = 1) { spyViewModel.emitAction(Back) }
    }
}
