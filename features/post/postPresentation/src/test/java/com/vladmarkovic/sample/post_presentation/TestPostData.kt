package com.vladmarkovic.sample.post_presentation

import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.feed.model.FeedPostItem

val fakeInitialPostsRange = IntRange(1, 4)
val fakeRefreshedPostsRange = IntRange(5, 9)

val fakeInitialPosts = makeFeedPosts(fakeInitialPostsRange)
val fakeInitialFeedPostItems = makeFeedPostItems(fakeInitialPostsRange)

val fakeRefreshedPosts = makeFeedPosts(fakeRefreshedPostsRange)
val fakeRefreshedFeedPostItems = makeFeedPostItems(fakeRefreshedPostsRange)

private fun makeFeedPosts(range: IntRange) = (range.first .. range.last).map {
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
