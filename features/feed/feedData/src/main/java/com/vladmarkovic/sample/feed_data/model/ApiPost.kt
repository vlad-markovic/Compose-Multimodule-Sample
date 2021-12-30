package com.vladmarkovic.sample.feed_data.model

import com.vladmarkovic.sample.feed_domain.model.Post
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ApiPost(
    override val userId: Int,
    override val id: Int,
    override val title: String,
    @SerialName("body")
    override val content: String
): Post