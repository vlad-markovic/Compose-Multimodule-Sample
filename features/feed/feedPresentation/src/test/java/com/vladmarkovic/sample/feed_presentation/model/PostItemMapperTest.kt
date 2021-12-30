package com.vladmarkovic.sample.feed_presentation.model

import com.vladmarkovic.sample.feed_domain.model.Post
import com.vladmarkovic.sample.feed_presentation.fakeInitialPostItems
import com.vladmarkovic.sample.feed_presentation.fakeInitialPosts
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class PostItemMapperTest {

    companion object {
        private val mappedFakeInitialPostItems = fakeInitialPosts.items

        @JvmStatic
        @Suppress("Unused")
        fun args(): Stream<Arguments> = fakeInitialPosts.indices.map {
            Arguments.of(fakeInitialPosts[it], fakeInitialPostItems[it], mappedFakeInitialPostItems[it])
        }.stream()
    }

    @MethodSource("args")
    @DisplayName("Given list of posts")
    @ParameterizedTest(name = "When post is ''{0}'', Then item is ''{2}''")
    fun testRatesMapping(post: Post, expectedItem: PostItem, actualItem: PostItem) {
        assertEquals(expectedItem, actualItem)
    }
}
