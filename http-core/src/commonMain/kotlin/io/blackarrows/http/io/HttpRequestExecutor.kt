package io.blackarrows.http.io

import io.blackarrows.http.HttpHeaders
import io.blackarrows.http.HttpRequestConfig
import io.blackarrows.http.data.MultipartForm

interface HttpRequestExecutor {
    suspend fun postJson(
        url: String,
        body: Any,
        headers: HttpHeaders = HttpHeaders.Empty,
        authRequired: Boolean = false,
        config: HttpRequestConfig = HttpRequestConfig.Default,
    ): ApiResponse

    suspend fun postQuery(
        url: String,
        queryParams: Map<String, String>,
        contentType: String? = null,
        headers: HttpHeaders = HttpHeaders.Empty,
        authRequired: Boolean = false,
        config: HttpRequestConfig = HttpRequestConfig.Default,
    ): ApiResponse

    suspend fun postRaw(
        url: String,
        body: Any,
        contentType: String = "application/octet-stream",
        headers: HttpHeaders = HttpHeaders.Empty,
        authRequired: Boolean = false,
        config: HttpRequestConfig = HttpRequestConfig.Default,
    ): ApiResponse

    suspend fun getJson(
        url: String,
        queryParams: Map<String, String> = emptyMap(),
        headers: HttpHeaders = HttpHeaders.Empty,
        authRequired: Boolean = false,
        config: HttpRequestConfig = HttpRequestConfig.Default,
    ): ApiResponse

    suspend fun getRaw(
        url: String,
        headers: HttpHeaders = HttpHeaders.Empty,
        queryParams: Map<String, String> = emptyMap(),
        authRequired: Boolean = false,
        config: HttpRequestConfig = HttpRequestConfig.Default,
    ): ApiResponse

    suspend fun putJson(
        url: String,
        body: Any,
        headers: HttpHeaders = HttpHeaders.Empty,
        authRequired: Boolean = false,
        config: HttpRequestConfig = HttpRequestConfig.Default,
    ): ApiResponse

    suspend fun putRaw(
        url: String,
        body: Any,
        contentType: String = "application/octet-stream",
        headers: HttpHeaders = HttpHeaders.Empty,
        authRequired: Boolean = false,
        config: HttpRequestConfig = HttpRequestConfig.Default,
    ): ApiResponse

    /**
     * Sends a PATCH request with a JSON-serialized body.
     *
     * The default implementation throws [UnsupportedOperationException]: this method was
     * added in 1.2.0 as a default (not abstract) member so existing third-party
     * [HttpRequestExecutor] implementations keep compiling without adding PATCH support.
     * [io.blackarrows.http.ktor.KtorHttpRequestExecutor] overrides this with a real
     * implementation.
     */
    suspend fun patchJson(
        url: String,
        body: Any,
        headers: HttpHeaders = HttpHeaders.Empty,
        authRequired: Boolean = false,
        config: HttpRequestConfig = HttpRequestConfig.Default,
    ): ApiResponse = throw UnsupportedOperationException(
        "patchJson is not implemented by this HttpRequestExecutor. " +
            "If you're using KtorHttpRequestExecutor from http-ktor, update to 1.2.0+."
    )

    /**
     * Sends a PATCH request with a raw body and explicit content type.
     *
     * The default implementation throws [UnsupportedOperationException]; see [patchJson]
     * for why this is a default rather than an abstract member.
     */
    suspend fun patchRaw(
        url: String,
        body: Any,
        contentType: String = "application/octet-stream",
        headers: HttpHeaders = HttpHeaders.Empty,
        authRequired: Boolean = false,
        config: HttpRequestConfig = HttpRequestConfig.Default,
    ): ApiResponse = throw UnsupportedOperationException(
        "patchRaw is not implemented by this HttpRequestExecutor. " +
            "If you're using KtorHttpRequestExecutor from http-ktor, update to 1.2.0+."
    )

    suspend fun deleteJson(
        url: String,
        body: Any? = null,
        headers: HttpHeaders = HttpHeaders.Empty,
        authRequired: Boolean = false,
        config: HttpRequestConfig = HttpRequestConfig.Default,
    ): ApiResponse

    suspend fun deleteRaw(
        url: String,
        body: Any? = null,
        contentType: String = "application/octet-stream",
        headers: HttpHeaders = HttpHeaders.Empty,
        authRequired: Boolean = false,
        config: HttpRequestConfig = HttpRequestConfig.Default,
    ): ApiResponse

    suspend fun postForm(
        url: String,
        formParams: Map<String, String?>,
        headers: HttpHeaders = HttpHeaders.Empty,
        authRequired: Boolean = false,
        config: HttpRequestConfig = HttpRequestConfig.Default,
    ): ApiResponse

    suspend fun postMultipart(
        url: String,
        form: MultipartForm,
        headers: HttpHeaders = HttpHeaders.Empty,
        authRequired: Boolean = false,
        config: HttpRequestConfig = HttpRequestConfig.Default,
    ): ApiResponse
}
