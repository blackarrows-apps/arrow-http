package io.blackarrows.http.data

data class MultipartForm(
    val fields: Map<String, String>,
    val files: List<MultipartPart>,
)
