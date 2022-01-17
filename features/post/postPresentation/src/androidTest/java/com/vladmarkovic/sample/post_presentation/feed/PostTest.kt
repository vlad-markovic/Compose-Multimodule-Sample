package com.vladmarkovic.sample.post_presentation.feed

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.*
import com.vladmarkovic.sample.post_presentation.FakeAuthorRepository.Companion.FAKE_FETCH_DELAY
import com.vladmarkovic.sample.post_presentation.post.PostViewModel
import com.vladmarkovic.sample.post_presentation.post.compose.PostScreen
import com.vladmarkovic.sample.shared_android_test.TestCompose
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.ArgKeys
import com.vladmarkovic.sample.shared_presentation.ui.model.MainBottomTab
import com.vladmarkovic.sample.shared_test.TestDispatcherProvider
import com.vladmarkovic.sample.shared_test.TestNetworkConnectivity
import com.vladmarkovic.sample.shared_test.setupTestLogger
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class PostTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testDispatchers = TestDispatcherProvider(testDispatcher)
    private val fakePostRepository = FakePostRepository()
    private val fakeAuthorRepository = FakeAuthorRepository()
    private val mockSavedStateHandle: SavedStateHandle = mockk()
    private val testNetworkConnectivity = TestNetworkConnectivity()

    private lateinit var viewModel: PostViewModel

    @Before
    fun init() {
        setupTestLogger()
        every { mockSavedStateHandle.get<String>(ArgKeys.POST.name) }.returns(postJson)
        every { mockSavedStateHandle.set<Post>(ArgKeys.POST.name, fakePost) }.answers { }

        viewModel = PostViewModel(
            fakePostRepository,
            fakeAuthorRepository,
            testDispatchers,
            mockSavedStateHandle,
            testNetworkConnectivity
        )

        composeTestRule.setContent {
            TestCompose(
                tab = MainBottomTab.POSTS_TAB,
                viewModel = viewModel
            ) {
                PostScreen(fakePost, viewModel.authorResult, viewModel::getDetails, viewModel::deletePost)
            }
        }
    }

    @Test
    fun testFeedAndOpenPostScreen() {
        composeTestRule.onNodeWithText(fakePost.title).assertExists()
        composeTestRule.onNodeWithText(fakePost.title).performClick()

        composeTestRule.onNodeWithText(fakePost.title).assertExists()
        composeTestRule.onNodeWithText(fakePost.content).assertExists()
        composeTestRule.onNodeWithText(fakeAuthor.name).assertDoesNotExist()
        composeTestRule.onNodeWithText(fakeAuthor.email).assertDoesNotExist()
        composeTestRule.onNodeWithText("Delete Post").assertDoesNotExist()

        testDispatcher.advanceTimeBy(FAKE_FETCH_DELAY)

        composeTestRule.onNodeWithText(fakeAuthor.name).assertExists()
        composeTestRule.onNodeWithText(fakeAuthor.email).assertExists()
        composeTestRule.onNodeWithText("Delete Post").assertExists()
    }
}
