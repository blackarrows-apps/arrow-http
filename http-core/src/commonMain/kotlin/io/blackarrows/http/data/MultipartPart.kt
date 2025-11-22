package io.blackarrows.http.data

data class MultipartPart(
    val name: String,
    val value: ByteArray,
    val filename: String? = null,
    val contentType: String? = null,
)
