/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_data

import com.vladmarkovic.sample.post_data.model.DataAuthor
import com.vladmarkovic.sample.post_data.model.DataPost

class FakePostApi : PostApi {
    override suspend fun fetchAllPosts(): List<DataPost> = fakeApiPosts
    override suspend fun fetchAuthor(id: Int): DataAuthor = fakeApiAuthor
}
