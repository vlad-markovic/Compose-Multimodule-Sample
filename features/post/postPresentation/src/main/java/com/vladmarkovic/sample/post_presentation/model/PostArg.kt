package com.vladmarkovic.sample.post_presentation.model

import com.vladmarkovic.sample.post_domain.model.Post
import kotlinx.serialization.Serializable

@Serializable
data class PostArg(
    override val id: Int,
    override val userId: Int,
    override val title: String,
    override val content: String
) : Post

val Post.arg: PostArg get() = PostArg(id, userId, title, content)
