package com.vladmarkovic.sample.post_data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vladmarkovic.sample.post_data.model.DataPost
import com.vladmarkovic.sample.shared_data.model.LastSaved

@Database(entities = [DataPost::class, LastSaved::class], version = 1, exportSchema = false)
abstract class PostDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "post_database"
        const val POSTS_TABLE = "posts"
    }

    abstract fun postDao(): PostDao
}
