package com.vladmarkovic.sample.post_data.model

import com.vladmarkovic.sample.post_domain.model.Post
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiPost(
    override val userId: Int,
    override val id: Int,
    override val title: String,
    @SerialName("body")
    override val content: String
): Post
