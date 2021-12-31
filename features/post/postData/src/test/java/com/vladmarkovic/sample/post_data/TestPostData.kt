package com.vladmarkovic.sample.post_data

import com.vladmarkovic.sample.post_data.model.DataPost

val fakeDbPostsRange = IntRange(1, 4)
val fakeApiPostsRange = IntRange(5, 9)

val fakeDbPosts = makeDataPosts(fakeDbPostsRange)
val fakeApiPosts = makeDataPosts(fakeApiPostsRange)

private fun makeDataPosts(range: IntRange) = (range.first .. range.last).map {
    DataPost(1, 11 * it , "Post$it", "This is Post $it")
}
