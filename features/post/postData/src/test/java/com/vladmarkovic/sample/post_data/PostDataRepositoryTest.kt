/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_data

import com.vladmarkovic.sample.post_data.model.DataPost
import com.vladmarkovic.sample.shared_data.model.LastSaved
import com.vladmarkovic.sample.shared_domain.model.DataSource
import com.vladmarkovic.sample.shared_test.CustomizableAllTestSetupExtension
import com.vladmarkovic.sample.shared_test.TestSystem
import io.mockk.coVerify
import io.mockk.slot
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("ClassName")
@ExperimentalCoroutinesApi
class PostDataRepositoryTest {

    companion object {
        private val testDispatcher = UnconfinedTestDispatcher()

        @JvmField
        @RegisterExtension
        @Suppress("Unused")
        val testSetupExtension = CustomizableAllTestSetupExtension(testDispatcher)

        @JvmStatic
        @Suppress("Unused")
        fun postArgs(): Stream<Arguments> = listOf(
            Arguments.of(DataSource.UNSPECIFIED, "Remote", Pair(1, 0), fakeApiPosts),
            Arguments.of(DataSource.REMOTE, "Remote", Pair(1, 0), fakeApiPosts),
            Arguments.of(DataSource.CACHE, "Cache", Pair(0, 1), fakeDbPosts)
        ).stream()

        @JvmStatic
        @Suppress("Unused")
        fun authorArgs(): Stream<Arguments> = listOf(
            Arguments.of(true, "Remote"),
            Arguments.of(false, "Cache")
        ).stream()
    }

    private val testSystem = TestSystem(currentMillis = 1640924357881)

    private lateinit var fakeApi: PostApi
    private lateinit var fakeDao: PostDao

    private lateinit var spyApi: PostApi
    private lateinit var spyDao: PostDao

    private lateinit var postRepo: PostDataRepository
    private lateinit var authorRepo: AuthorDataRepository

    @BeforeEach
    fun setup() {
        fakeApi = FakePostApi()
        fakeDao = FakePostDao()

        spyApi = spyk(fakeApi)
        spyDao = spyk(fakeDao)

        postRepo = PostDataRepository(spyApi, spyDao, testSystem)
        authorRepo = AuthorDataRepository(spyApi, spyDao, testSystem)
    }

    @Nested
    inner class FetchingPostsTests {

        @MethodSource("com.vladmarkovic.sample.post_data.PostDataRepositoryTest#postArgs")
        @ParameterizedTest(name = "When force fetch source is ''{0}'', posts are fetched from the ''{1}''")
        @DisplayName("Given fetching posts after initially saved and cache time expired")
        fun testFetchingPostsAfterSavedAndCacheExpiredAndForceRefresh(
            forceFetch: DataSource,
            source: String,
            times: Pair<Int, Int>,
            posts: List<DataPost>
        ) {
            runTest {
                spyDao.insertPosts(fakeDbPosts)
                spyDao.updateLastSaved(getLastSaved(PostDatabase.POSTS_TABLE))

                setExpiredCache(PostDatabase.POSTS_TABLE)

                postRepo.fetchAllPosts(forceFetch)

                coVerify(exactly = times.first) { spyApi.fetchAllPosts() }
                coVerify(exactly = times.second) { spyDao.getAllPosts() }
                assertEquals(posts, spyDao.getAllPosts())
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
        inner class Given_fetching_posts_with_source_unspecified {

            @Nested
            @TestMethodOrder(MethodOrderer.OrderAnnotation::class)
            @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
            inner class When_cache_time_has_expired {

                @BeforeEach
                fun setup() {
                    setExpiredCache(PostDatabase.POSTS_TABLE)
                    runTest {
                        postRepo.fetchAllPosts(DataSource.UNSPECIFIED)
                    }
                }

                @Test
                @Order(1)
                @DisplayName("Then it fetches posts from remote")
                fun thenItFetchesPostsFromRemote() {
                    runTest {
                        coVerify(exactly = 1) { spyApi.fetchAllPosts() }
                    }
                }

                @Test
                @Order(2)
                @DisplayName("And it does NOT fetch posts from cache")
                fun andItDoesNotFetchPostsFromCache() {
                    runTest {
                        coVerify(exactly = 0) { spyDao.getAllPosts() }
                    }
                }
            }

            @Nested
            @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
            inner class When_cache_time_has_NOT_expired {

                @BeforeEach
                fun setup() {
                    setNotExpiredCache(PostDatabase.POSTS_TABLE)
                    runTest {
                        postRepo.fetchAllPosts(DataSource.UNSPECIFIED)
                    }
                }

                @Test
                @DisplayName("Then it fetches posts from the cache")
                fun thenItFetchesPostsFromRemote() {
                    runTest {
                        coVerify(exactly = 1) { spyDao.getAllPosts() }
                    }
                }

                @Test
                @DisplayName("And it does not fetch posts from the remote")
                fun andItDoesNotFetchPostsFromCache() {
                    runTest {
                        coVerify(exactly = 0) { spyApi.fetchAllPosts() }
                    }
                }
            }
        }

        @Nested
        @TestMethodOrder(MethodOrderer.OrderAnnotation::class)
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
        inner class Given_force_fetching_posts_from_cache {

            @BeforeEach
            fun setup() {
                runTest {
                    postRepo.fetchAllPosts(DataSource.CACHE)
                }
            }

            @Order(1)
            @ValueSource(strings = ["has", "has NOT"])
            @ParameterizedTest(name = "If time ''{0}'' passed cache expiry")
            @DisplayName("Then it fetches posts from cache, no mater the time elapsed")
            fun thenItAlwaysFetchesPostsFromRemote(expired: String) {
                if (expired == "has") setExpiredCache(PostDatabase.POSTS_TABLE)
                else setNotExpiredCache(PostDatabase.POSTS_TABLE)

                runTest {
                    postRepo.fetchAllPosts(DataSource.CACHE)
                }

                coVerify(exactly = 2) { spyDao.getAllPosts() }
            }

            @Test
            @Order(2)
            @DisplayName("And it does not fetch posts from remote")
            fun andItDoesNotFetchPostsFromCache() {
                runTest {
                    coVerify(exactly = 0) { spyApi.fetchAllPosts() }
                }
            }
        }

        @Nested
        @TestMethodOrder(MethodOrderer.OrderAnnotation::class)
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
        inner class Given_force_refreshing_posts_from_remote {

            @BeforeEach
            fun setup() {
                runTest {
                    postRepo.fetchAllPosts(DataSource.REMOTE)
                }
            }

            @Order(1)
            @ValueSource(strings = ["has", "has NOT"])
            @ParameterizedTest(name = "If time ''{0}'' passed cache expiry")
            @DisplayName("Then it fetches posts from remote, no mater the time elapsed")
            fun thenItAlwaysFetchesPostsFromRemote(expired: String) {
                if (expired == "has") setExpiredCache(PostDatabase.POSTS_TABLE)
                else setNotExpiredCache(PostDatabase.POSTS_TABLE)

                runTest {
                    postRepo.fetchAllPosts(DataSource.REMOTE)
                }

                coVerify(exactly = 2) { spyApi.fetchAllPosts() }
            }

            @Test
            @Order(2)
            @DisplayName("And it does not fetch posts from the cache")
            fun andItDoesNotFetchPostsFromCache() {
                runTest {
                    coVerify(exactly = 0) { spyDao.getAllPosts() }
                }
            }

            @Test
            @Order(3)
            @DisplayName("And it clears current posts cache")
            fun andItClearsCurrentPostsCache() {
                runTest {
                    coVerify(exactly = 1) { spyDao.deleteAllPosts() }
                }
            }

            @Test
            @Order(4)
            @DisplayName("And it clears current authors cache")
            fun andItClearsCurrentAuthorsCache() {
                runTest {
                    coVerify(exactly = 1) { spyDao.deleteAllAuthors() }
                }
            }

            @Test
            @Order(5)
            @DisplayName("And it clears current last saved cache")
            fun andItClearsCurrentLastSavedCache() {
                runTest {
                    coVerify(exactly = 1) { spyDao.deleteAllLastSaved() }
                }
            }

            @Test
            @Order(6)
            @DisplayName("And it caches newly fetched posts")
            fun andItCachesNewlyFetchedPosts() {
                runTest {
                    coVerify(exactly = 1) { spyDao.insertPosts(fakeApiPosts) }
                    assertEquals(fakeApiPosts, spyDao.getAllPosts())
                }
            }

            @Test
            @Order(7)
            @DisplayName("And saves time posts were last cached")
            fun andItSavesTimePostsWereLastCached() {
                runTest {
                    val lastSaved = getLastSaved(PostDatabase.POSTS_TABLE)
                    val lastSavedSlot = slot<LastSaved>()
                    coVerify(exactly = 1) { spyDao.updateLastSaved(capture(lastSavedSlot)) }
                    assertEquals(lastSaved, lastSavedSlot.captured)
                    assertEquals(lastSaved, spyDao.getLastSaved(PostDatabase.POSTS_TABLE))
                }
            }
        }
    }

    @Nested
    inner class FetchingAuthorTests {

        @Test
        @DisplayName(
            "Given initially fetching author, " +
                    "When the posts have not yet been saved to database, " +
                    "It fetches from the api, no matter the cache expiry"
        )
        fun testInitiallyFetchingAuthor() {
            setExpiredCache(PostDatabase.AUTHORS_TABLE)

            runTest {
                authorRepo.fetchAuthor(fakeApiAuthor.id)

                coVerify(exactly = 1) { spyApi.fetchAuthor(fakeApiAuthor.id) }
                coVerify(exactly = 0) { spyDao.getAuthor(fakeApiAuthor.id) }
            }
        }

        @Test
        @DisplayName("Given fetching author from api, It gets saved into database.")
        fun testCachingAuthor() {
            runTest {
                authorRepo.fetchAuthor(fakeApiAuthor.id)

                coVerify(exactly = 1) { spyApi.fetchAuthor(fakeApiAuthor.id) }

                coVerify(exactly = 1) { spyDao.insertAuthor(fakeApiAuthor) }
                // Verify saved author
                assertEquals(fakeApiAuthor, spyDao.getAuthor(fakeApiAuthor.id))
                // Verify last saved
                val lastSaved = getLastSaved(PostDatabase.AUTHORS_TABLE)
                val lastSavedSlot = slot<LastSaved>()
                coVerify(exactly = 1) { spyDao.updateLastSaved(capture(lastSavedSlot)) }
                assertEquals(lastSaved, lastSavedSlot.captured)
                assertEquals(lastSaved, spyDao.getLastSaved(PostDatabase.AUTHORS_TABLE))
            }
        }

        @MethodSource("com.vladmarkovic.sample.post_data.PostDataRepositoryTest#authorArgs")
        @ParameterizedTest(name = "When cache expired is ''{0}'', author is fetched from the ''{1}''")
        @DisplayName("Given fetching author, after initially saved before")
        fun testFetchingAuthorAfterSavedAndCacheExpired(expireCache: Boolean, source: String) {
            runTest {
                spyDao.insertAuthor(fakeDbAuthor)
                spyDao.updateLastSaved(getLastSaved(PostDatabase.AUTHORS_TABLE))

                if (expireCache) {
                    setExpiredCache(PostDatabase.AUTHORS_TABLE)
                }

                authorRepo.fetchAuthor(fakeDbAuthor.id)

                val remoteTimes = if (expireCache) 1 else 0
                val cacheTimes = if (expireCache) 0 else 1

                coVerify(exactly = remoteTimes) { spyApi.fetchAuthor(any()) }
                coVerify(exactly = cacheTimes) { spyDao.getAuthor(fakeDbAuthor.id) }
                assertEquals(fakeDbAuthor, spyDao.getAuthor(fakeDbAuthor.id))
            }
        }
    }

    private fun getLastSaved(tableName: String, millis: Long = testSystem.currentMillis):
            LastSaved = LastSaved(tableName, millis)

    private fun setExpiredCache(tableName: String) {
        runTest {
            spyDao.updateLastSaved(getLastSaved(tableName, 0))
            assertEquals(0, spyDao.getLastSaved(tableName)?.time)
            assertTrue(postRepo.cacheExpired(tableName))
        }
    }

    @Suppress("SameParameterValue")
    private fun setNotExpiredCache(tableName: String) {
        runTest {
            spyDao.updateLastSaved(getLastSaved(tableName))
            assertEquals(testSystem.currentMillis, spyDao.getLastSaved(tableName)?.time)
            assertFalse(postRepo.cacheExpired(tableName))
        }
    }
}
