/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_domain

import com.vladmarkovic.sample.shared_domain.model.DataSource
import com.vladmarkovic.sample.post_domain.model.Post

interface PostRepository {
    suspend fun fetchAllPosts(forceFetch: DataSource = DataSource.UNSPECIFIED): List<Post>
}
