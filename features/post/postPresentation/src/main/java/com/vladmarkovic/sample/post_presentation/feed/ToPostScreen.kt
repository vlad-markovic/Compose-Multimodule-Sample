package com.vladmarkovic.sample.post_presentation.feed

import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction

data class ToPostScreen(val post: Post) : NavigationAction
