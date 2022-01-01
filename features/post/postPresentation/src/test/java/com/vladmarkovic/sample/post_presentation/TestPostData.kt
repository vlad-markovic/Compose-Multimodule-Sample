package com.vladmarkovic.sample.post_presentation

import com.vladmarkovic.sample.post_domain.model.Author
import com.vladmarkovic.sample.post_domain.model.Post

// region feed
val fakeInitialPostsRange = IntRange(1, 4)
val fakeRefreshedPostsRange = IntRange(5, 9)

val fakeInitialPosts = makeFakePosts(fakeInitialPostsRange)
val fakeRefreshedPosts = makeFakePosts(fakeRefreshedPostsRange)

private fun makeFakePosts(range: IntRange) = (range.first .. range.last).map {
    makeFakePost(it)
}

private fun makeFakePost(id: Int) =
    TestPost(id, 11 * id , "Post$id", "This is Post $id")

data class TestPost(
    override val id: Int,
    override val userId: Int,
    override val title: String,
    override val content: String,
) : Post
// region feed

// region post
val fakeAuthor = TestAuthor(100, "David", "Dave", "dave@email.com")
val fakePost = makeFakePost(123)
val fakeAuthorSuccessResult = Result.success(fakeAuthor)

data class TestAuthor(
    override val id: Int,
    override val name: String,
    override val username: String,
    override val email: String,
) : Author
// region post
