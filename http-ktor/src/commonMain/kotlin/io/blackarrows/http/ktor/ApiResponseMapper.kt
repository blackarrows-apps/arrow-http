package io.blackarrows.http.ktor

import io.blackarrows.http.io.ApiResponse
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray

suspend fun HttpResponse.toApiResponse(): ApiResponse {
    val byteArray = bodyAsChannel().readRemaining().readByteArray()
    val headerMap = headers.entries().associate { it.key to it.value.joinToString(",") }

    return KtorApiResponse(
        statusCode = status.value,
        body = byteArray,
        headers = headerMap,
    )
}
