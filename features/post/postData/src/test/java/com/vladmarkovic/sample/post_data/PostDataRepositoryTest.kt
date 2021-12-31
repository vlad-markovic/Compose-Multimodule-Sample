package com.vladmarkovic.sample.post_data

import com.vladmarkovic.sample.post_data.model.DataPost
import com.vladmarkovic.sample.shared_data.model.LastSaved
import com.vladmarkovic.sample.shared_test.CustomizableAllTestSetupExtension
import com.vladmarkovic.sample.shared_test.TestSystem
import com.vladmarkovic.sample.shared_test.setupCoroutines
import io.mockk.coVerify
import io.mockk.slot
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class PostDataRepositoryTest {

    companion object {
        @JvmField
        @RegisterExtension
        @Suppress("unused")
        val testSetupExtension = CustomizableAllTestSetupExtension()
            .setupCoroutines()

        @JvmStatic
        @Suppress("Unused")
        fun args(): Stream<Arguments> = listOf(
            Arguments.of(false, "database", Pair(0, 1), fakeDbPosts),
            Arguments.of(true, "api", Pair(1, 0), fakeApiPosts)
        ).stream()
    }

    private val testSystem = TestSystem(currentMillis = 1640924357881)

    private lateinit var fakeApi: PostApi
    private lateinit var fakeDao: PostDao

    private lateinit var spyApi: PostApi
    private lateinit var spyDao: PostDao

    private lateinit var repo: PostDataRepository

    @BeforeEach
    fun setup() {
        fakeApi = FakePostApi()
        fakeDao = FakePostDao()

        spyApi = spyk(fakeApi)
        spyDao = spyk(fakeDao)

        repo = PostDataRepository(spyApi, spyDao, testSystem)
    }

    @ValueSource(booleans = [false, true])
    @ParameterizedTest(name = "When force refresh is ''{0}'', Then it fetches from the api")
    @DisplayName("Given initially fetching posts, no matter the cache expiry")
    fun testForceRefresh(forceRefresh: Boolean) {
        advanceTimeBy(PostDataRepository.CACHE_EXPIRY_MILLIS)

        runBlockingTest {
            repo.fetchAllPosts(forceRefresh)

            coVerify(exactly = 1) { spyApi.fetchAllPosts() }
            coVerify(exactly = 0) { spyDao.getAllPosts() }
        }
    }

    @ValueSource(booleans = [false, true])
    @ParameterizedTest(name = "When force refresh is ''{0}'', posts are saved to the database")
    @DisplayName("Given fetching posts from api")
    fun testSaveToCache(forceRefresh: Boolean) {
        runBlockingTest {
            repo.fetchAllPosts(forceRefresh)

            coVerify(exactly = 1) { spyApi.fetchAllPosts() }
            coVerify(exactly = 1) { spyDao.deleteAllPosts() }
            coVerify(exactly = 1) { spyDao.insertPosts(fakeApiPosts) }
            val lastSaved = getLastSaved()
            val lastSavedSlot = slot<LastSaved>()
            coVerify(exactly = 1) { spyDao.updateLastSaved(capture(lastSavedSlot)) }
            assertEquals(lastSaved, lastSavedSlot.captured)
            assertEquals(lastSaved, spyDao.getLastSaved(PostDatabase.POSTS_TABLE))
        }
    }

    @MethodSource("args")
    @ParameterizedTest(name = "When force refresh is ''{0}'', posts are fetched from the ''{1}''")
    @DisplayName("Given fetching posts after initially saved and cache time expired")
    fun testFetchingAfterSavedAndCacheExpired(
        forceRefresh: Boolean,
        apiOrDb: String,
        times: Pair<Int, Int>,
        posts: List<DataPost>
    ) {
        runBlockingTest {
            spyDao.insertPosts(fakeDbPosts)
            spyDao.updateLastSaved(getLastSaved())

            advanceTimeBy(PostDataRepository.CACHE_EXPIRY_MILLIS)

            repo.fetchAllPosts(forceRefresh)

            coVerify(exactly = times.first) { spyApi.fetchAllPosts() }
            coVerify(exactly = times.second) { spyDao.getAllPosts() }

            assertEquals(posts, spyDao.getAllPosts())
        }
    }

    private fun advanceTimeBy(millis: Long) {
        testSystem.currentMillis = testSystem.currentMillis + millis
    }

    private fun getLastSaved(): LastSaved =
        LastSaved(PostDatabase.POSTS_TABLE, testSystem.currentMillis)

    class FakePostApi : PostApi {
        override suspend fun fetchAllPosts(): List<DataPost> = fakeApiPosts
    }

    class FakePostDao : PostDao {
        private var posts: MutableList<DataPost> = mutableListOf()

        private var lastSaved: LastSaved? = null

        override suspend fun getAllPosts(): List<DataPost> = posts

        override suspend fun insertPosts(posts: List<DataPost>) {
            this.posts.addAll(posts)
        }

        override suspend fun deleteAllPosts() = posts.clear()

        override suspend fun getLastSaved(what: String): LastSaved? =
            if (what == lastSaved?.what) lastSaved else null

        override suspend fun updateLastSaved(lastSaved: LastSaved) {
            this.lastSaved = lastSaved
        }
    }
}
