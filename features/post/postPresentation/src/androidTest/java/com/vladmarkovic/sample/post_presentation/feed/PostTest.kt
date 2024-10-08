package com.vladmarkovic.sample.post_presentation.feed

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.post.PostViewModel
import com.vladmarkovic.sample.post_presentation.post.compose.PostScreen
import com.vladmarkovic.sample.shared_android_test.TestCompose
import com.vladmarkovic.sample.shared_domain.tab.MainBottomTab
import com.vladmarkovic.sample.shared_presentation.screen.ScreenArgNames
import com.vladmarkovic.sample.shared_test.TestNetworkConnectivity
import com.vladmarkovic.sample.shared_test.setupTestLogger
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PostTest {

    @get:Rule
    var composeTestRule = createComposeRule().apply {
        mainClock.autoAdvance = false
    }

    private val fakePostRepository = FakePostRepository()
    private val fakeAuthorRepository = FakeAuthorRepository()
    private val mockSavedStateHandle: SavedStateHandle = mockk()
    private val testNetworkConnectivity = TestNetworkConnectivity()

    private lateinit var viewModel: PostViewModel

    @Before
    fun init() {
        setupTestLogger()
        every { mockSavedStateHandle.get<String>(ScreenArgNames.POST.name) }.returns(postJson)
        every { mockSavedStateHandle.set<Post>(ScreenArgNames.POST.name, fakePost) }.answers { }

        composeTestRule.mainClock.autoAdvance = false

        viewModel = PostViewModel(
            fakePostRepository,
            fakeAuthorRepository,
            mockSavedStateHandle,
            testNetworkConnectivity
        )

        composeTestRule.setContent {
            TestCompose(
                tab = MainBottomTab.POSTS_TAB,
                viewModel = viewModel
            ) { navController, modifier, bubbleUp ->
                val authorResult = viewModel.authorResult.collectAsStateWithLifecycle()

                PostScreen(fakePost, authorResult.value, viewModel::getDetails, viewModel::deletePost)
            }
        }
    }

    @Test
    fun testFeedAndOpenPostScreen() {
        composeTestRule.onNodeWithText(fakePost.title).assertExists()
        composeTestRule.onNodeWithText(fakePost.title).performClick()

        composeTestRule.onNodeWithText(fakePost.title).assertExists()
        composeTestRule.onNodeWithText(fakePost.content).assertExists()

        composeTestRule.onNodeWithText(fakeAuthor.name).assertExists()
        composeTestRule.onNodeWithText(fakeAuthor.email).assertExists()
        composeTestRule.onNodeWithText("Delete Post").assertExists()
    }
}
