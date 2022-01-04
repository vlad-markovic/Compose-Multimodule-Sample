/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_data.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vladmarkovic.sample.post_data.PostDatabase
import com.vladmarkovic.sample.post_domain.model.Post
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = PostDatabase.POSTS_TABLE)
@Serializable
data class DataPost(
    @PrimaryKey
    override val id: Int,
    @NonNull
    override val userId: Int,
    @NonNull
    override val title: String,
    @NonNull @SerialName("body")
    override val content: String
) : Post
