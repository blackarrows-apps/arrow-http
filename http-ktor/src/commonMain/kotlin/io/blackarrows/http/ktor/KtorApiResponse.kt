package io.blackarrows.http.ktor

import io.blackarrows.http.io.ApiResponse

class KtorApiResponse(
    override val statusCode: Int,
    override val body: ByteArray?,
    override val headers: Map<String, String>,
) : ApiResponse {
    override fun isSuccessful(): Boolean = statusCode in 200..299
}
