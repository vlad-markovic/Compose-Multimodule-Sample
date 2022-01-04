/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_data.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vladmarkovic.sample.post_data.PostDatabase
import com.vladmarkovic.sample.post_domain.model.Author
import kotlinx.serialization.Serializable

@Entity(tableName = PostDatabase.AUTHORS_TABLE)
@Serializable
data class DataAuthor(
    @PrimaryKey
    override val id: Int,
    @NonNull
    override val name: String,
    @NonNull
    override val username: String,
    @NonNull
    override val email: String
) : Author
