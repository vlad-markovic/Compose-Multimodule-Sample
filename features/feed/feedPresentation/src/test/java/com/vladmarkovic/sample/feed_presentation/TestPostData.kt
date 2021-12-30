package com.vladmarkovic.sample.feed_presentation

import com.vladmarkovic.sample.feed_domain.model.Post
import com.vladmarkovic.sample.feed_presentation.model.PostItem

val fakeInitialPostsRange = IntRange(1, 4)
val fakeFetchedPostsRange = IntRange(5, 9)

val fakeInitialPosts = makePosts(fakeInitialPostsRange)
val fakeInitialPostItems = makePostItems(fakeInitialPostsRange)

val fakeRefreshedPosts = makePosts(fakeFetchedPostsRange)
val fakeRefreshedPostItems = makePostItems(fakeFetchedPostsRange)

private fun makePosts(range: IntRange) = (range.first .. range.last).map {
    FakePost(1, 11 * it , "Post$it", "This is Post $it")
}

private fun makePostItems(range: IntRange) = (range.first .. range.last).map {
    PostItem("Post$it", "This is Post $it")
}

data class FakePost(
    override val id: Int,
    override val userId: Int,
    override val title: String,
    override val content: String,
) : Post
