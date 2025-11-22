package io.blackarrows.http.io

interface ApiResponse {
    val statusCode: Int
    val body: ByteArray?
    val headers: Map<String, String>

    fun isSuccessful(): Boolean = statusCode in 200..299
}
