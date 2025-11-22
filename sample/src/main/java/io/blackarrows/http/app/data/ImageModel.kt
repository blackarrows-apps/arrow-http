package io.blackarrows.http.app.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageModel(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    @SerialName("download_url")
    val downloadUrl: String
)
