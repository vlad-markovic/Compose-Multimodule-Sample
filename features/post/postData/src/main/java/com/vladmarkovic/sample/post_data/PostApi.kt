package com.vladmarkovic.sample.post_data

import com.vladmarkovic.sample.post_data.model.DataPost

interface PostApi {
    suspend fun fetchAllPosts(): List<DataPost>
}
