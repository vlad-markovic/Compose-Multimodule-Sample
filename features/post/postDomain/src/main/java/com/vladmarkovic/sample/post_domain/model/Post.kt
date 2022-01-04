/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.post_domain.model

import java.io.Serializable

interface Post : Serializable {
    val userId: Int
    val id: Int
    val title: String
    val content: String
}
