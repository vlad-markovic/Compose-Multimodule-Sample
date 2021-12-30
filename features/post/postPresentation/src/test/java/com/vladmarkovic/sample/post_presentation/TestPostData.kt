package com.vladmarkovic.sample.post_presentation

import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.feed.model.FeedPostItem

val fakeInitialPostsRange = IntRange(1, 4)
val fakeFetchedPostsRange = IntRange(5, 9)

val fakeInitialPosts = makePosts(fakeInitialPostsRange)
val fakeInitialFeedPostItems = makeFeedPostItems(fakeInitialPostsRange)

val fakeRefreshedPosts = makePosts(fakeFetchedPostsRange)
val fakeRefreshedFeedPostItems = makeFeedPostItems(fakeFetchedPostsRange)

private fun makePosts(range: IntRange) = (range.first .. range.last).map {
    TestPost(1, 11 * it , "Post$it", "This is Post $it")
}

private fun makeFeedPostItems(range: IntRange) = (range.first .. range.last).map {
    FeedPostItem("Post$it", "This is Post $it")
}

data class TestPost(
    override val id: Int,
    override val userId: Int,
    override val title: String,
    override val content: String,
) : Post
