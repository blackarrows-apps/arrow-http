package io.blackarrows.http.app.data

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Int? = null,
    val userId: Int,
    val title: String,
    val body: String
)

@Serializable
data class CreatePostRequest(
    val userId: Int,
    val title: String,
    val body: String
)
