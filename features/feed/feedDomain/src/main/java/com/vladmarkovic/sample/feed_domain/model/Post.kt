package com.vladmarkovic.sample.feed_domain.model

interface Post {
    val userId: Int
    val id: Int
    val title: String
    val content: String
}
