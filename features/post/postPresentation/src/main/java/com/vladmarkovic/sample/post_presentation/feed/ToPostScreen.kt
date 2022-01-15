package com.vladmarkovic.sample.post_presentation.feed

import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.shared_presentation.navigation.ToScreen

data class ToPostScreen(val post: Post) : ToScreen
