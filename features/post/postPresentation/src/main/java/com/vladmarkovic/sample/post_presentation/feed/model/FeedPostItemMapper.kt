package com.vladmarkovic.sample.post_presentation.feed.model

import com.vladmarkovic.sample.post_domain.model.Post

val List<Post>.items: List<FeedPostItem> get() = map { FeedPostItem(it.title, it.content) }
