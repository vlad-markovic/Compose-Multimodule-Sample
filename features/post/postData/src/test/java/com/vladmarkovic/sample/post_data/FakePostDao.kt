/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_data

import com.vladmarkovic.sample.post_data.model.DataAuthor
import com.vladmarkovic.sample.post_data.model.DataPost
import com.vladmarkovic.sample.shared_data.model.LastSaved

class FakePostDao : PostDao {
    // region posts
    private var posts: MutableList<DataPost> = mutableListOf()


    override suspend fun getAllPosts(): List<DataPost> = posts

    override suspend fun insertPosts(posts: List<DataPost>) {
        this.posts.addAll(posts)
    }

    override suspend fun deleteAllPosts() = posts.clear()
    // endregion posts

    // region author
    private var authors: MutableSet<DataAuthor> = mutableSetOf()

    override suspend fun getAuthor(id: Int): DataAuthor? = authors.find { it.id == id }

    override suspend fun insertAuthor(author: DataAuthor) {
          authors.add(author)
    }

    override suspend fun deleteAllAuthors() {
        authors.clear()
    }
    // endregion author

    // region last saved
    private var lastSaved: MutableList<LastSaved> = mutableListOf()

    override suspend fun getLastSaved(what: String): LastSaved? = lastSaved.find { it.what == what }

    override suspend fun updateLastSaved(lastSaved: LastSaved) {
        this.lastSaved.removeIf { it.what == lastSaved.what  }
        this.lastSaved.add(lastSaved)
    }

    override suspend fun deleteAllLastSaved() {
        lastSaved.clear()
    }
    // endregion last saved
}
