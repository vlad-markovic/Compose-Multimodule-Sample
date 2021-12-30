package com.vladmarkovic.sample.post_presentation.feed.model

import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.fakeInitialFeedPostItems
import com.vladmarkovic.sample.post_presentation.fakeInitialPosts
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class FeedPostItemMapperTest {

    companion object {
        private val mappedFakeInitialPostItems = fakeInitialPosts.items

        @JvmStatic
        @Suppress("Unused")
        fun args(): Stream<Arguments> = fakeInitialPosts.indices.map {
            Arguments.of(fakeInitialPosts[it], fakeInitialFeedPostItems[it], mappedFakeInitialPostItems[it])
        }.stream()
    }

    @MethodSource("args")
    @DisplayName("Given list of posts")
    @ParameterizedTest(name = "When post is ''{0}'', Then item is ''{2}''")
    fun testRatesMapping(post: Post, expectedItem: FeedPostItem, actualItem: FeedPostItem) {
        assertEquals(expectedItem, actualItem)
    }
}
