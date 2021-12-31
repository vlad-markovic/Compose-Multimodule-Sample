package com.vladmarkovic.sample.post_data

import androidx.room.*
import com.vladmarkovic.sample.post_data.model.DataPost
import com.vladmarkovic.sample.shared_data.model.LastSaved

@Dao
interface PostDao {

    // region table posts
    @Query("SELECT * FROM posts")
    suspend fun getAllPosts(): List<DataPost>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<DataPost>)

    @Query("DELETE FROM posts")
    suspend fun deleteAllPosts()
    // endregion table posts

    // region table LastSaved
    @Insert(entity = LastSaved::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLastSaved(lastSaved: LastSaved)

    @Query("SELECT * FROM LastSaved WHERE what = :what")
    suspend fun getLastSaved(what: String): LastSaved?
    // endregion table LastSaved
}
