/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_data

import com.vladmarkovic.sample.post_data.model.DataAuthor
import com.vladmarkovic.sample.post_data.model.DataPost

val fakeApiPostsRange = IntRange(1, 4)
val fakeDbPostsRange = IntRange(5, 9)

val fakeApiPosts = makeDataPosts(fakeApiPostsRange)
val fakeDbPosts = makeDataPosts(fakeDbPostsRange)

private fun makeDataPosts(range: IntRange) = (range.first .. range.last).map {
    DataPost(1, 11 * it , "Post$it", "This is Post $it")
}

val fakeApiAuthor = DataAuthor(100, "api", "api", "api@email.com")
val fakeDbAuthor = DataAuthor(200, "database", "db", "db@email.com")
