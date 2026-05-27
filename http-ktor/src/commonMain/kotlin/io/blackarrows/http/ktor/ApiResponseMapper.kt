package io.blackarrows.http.ktor

import io.blackarrows.http.io.ApiResponse
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray

// @OptIn(InternalAPI::class) can't be used here because the import path for
// InternalAPI differs across Ktor minor versions and the wasmJs compilation fails
// to resolve it. @Suppress achieves the same result without an import.
@Suppress("OPT_IN_USAGE_ERROR")
suspend fun HttpResponse.toApiResponse(): ApiResponse {
    // Use `rawContent` (the truly raw engine channel) instead of `bodyAsChannel()`.
    // `bodyAsChannel()` calls `body<ByteReadChannel>()` which wraps the channel with a
    // Content-Length limit when that header is present. On browsers the fetch API
    // auto-decompresses gzip but keeps the original *compressed* Content-Length, so the
    // decompressed body size exceeds the limit and Ktor throws "Content-Length mismatch".
    // `rawContent` bypasses that check and is safe on all platforms: OkHttp (Android/JVM)
    // decompresses before Ktor sees the bytes, so the data is correct everywhere.
    @Suppress("OPT_IN_USAGE_ERROR")
    val byteArray = rawContent.readRemaining().readByteArray()
    val headerMap = headers.entries().associate { it.key to it.value.joinToString(",") }

    return KtorApiResponse(
        statusCode = status.value,
        body = byteArray,
        headers = headerMap,
    )
}
