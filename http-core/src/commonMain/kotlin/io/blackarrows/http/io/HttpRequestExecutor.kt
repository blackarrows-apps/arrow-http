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
