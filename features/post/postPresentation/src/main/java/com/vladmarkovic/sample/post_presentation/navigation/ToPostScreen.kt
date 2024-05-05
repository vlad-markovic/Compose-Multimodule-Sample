/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_presentation.navigation

import com.vladmarkovic.sample.post_domain.model.Post
import com.vladmarkovic.sample.post_presentation.model.PostArg
import com.vladmarkovic.sample.post_presentation.model.arg
import com.vladmarkovic.sample.shared_presentation.navigation.ToScreen
import com.vladmarkovic.sample.shared_domain.screen.MainScreen.PostsScreen.POST_SCREEN
import kotlinx.serialization.json.Json

data class ToPostScreen(val post: Post) : ToScreen(
    POST_SCREEN,
    listOf(Json.encodeToString(PostArg.serializer(), post.arg))
)
