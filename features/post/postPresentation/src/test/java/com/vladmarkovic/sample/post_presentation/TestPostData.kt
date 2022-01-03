/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation

import com.vladmarkovic.sample.post_domain.model.Author
import com.vladmarkovic.sample.post_presentation.model.PostArg

// region feed
val fakeInitialPostsRange = IntRange(1, 4)
val fakeRefreshedPostsRange = IntRange(5, 9)

val fakeInitialPosts = makeFakePosts(fakeInitialPostsRange)
val fakeRefreshedPosts = makeFakePosts(fakeRefreshedPostsRange)

private fun makeFakePosts(range: IntRange) = (range.first .. range.last).map {
    makeFakePost(it)
}

private fun makeFakePost(id: Int) =
    PostArg(id, 11 * id , "Post$id", "This is Post $id")
// region feed

// region post
val fakeAuthor = TestAuthor(100, "David", "Dave", "dave@email.com")
const val fakePostId = 123
const val fakePostUserId = 456
const val fakePostTitle = "Fake post title"
const val fakePostContent = "Fake post content"
val fakePost = PostArg(fakePostId, fakePostUserId, fakePostTitle, fakePostContent)
val fakeAuthorSuccessResult = Result.success(fakeAuthor)

data class TestAuthor(
    override val id: Int,
    override val name: String,
    override val username: String,
    override val email: String,
) : Author
// region post
