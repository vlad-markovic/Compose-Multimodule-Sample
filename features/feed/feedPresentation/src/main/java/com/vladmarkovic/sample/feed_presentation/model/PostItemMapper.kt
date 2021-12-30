package com.vladmarkovic.sample.feed_presentation.model

import com.vladmarkovic.sample.feed_domain.model.Post

val List<Post>.items: List<PostItem> get() = map { PostItem(it.title, it.content) }
