package io.blackarrows.http.io

import io.blackarrows.http.HttpHeaders
import io.blackarrows.http.HttpRequestConfig
import io.blackarrows.http.data.MultipartForm
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

/**
 * A bare, non-Ktor [HttpRequestExecutor] that only implements the abstract methods.
 * Used to confirm the interface's default `patchJson`/`patchRaw` behaviour without
 * depending on `http-ktor`.
 */
private class BareHttpRequestExecutor : HttpRequestExecutor {
    override suspend fun postJson(
        url: String,
        body: Any,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = throw NotImplementedError()

    override suspend fun postQuery(
        url: String,
        queryParams: Map<String, String>,
        contentType: String?,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = throw NotImplementedError()

    override suspend fun postRaw(
        url: String,
        body: Any,
        contentType: String,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = throw NotImplementedError()

    override suspend fun getJson(
        url: String,
        queryParams: Map<String, String>,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = throw NotImplementedError()

    override suspend fun getRaw(
        url: String,
        headers: HttpHeaders,
        queryParams: Map<String, String>,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = throw NotImplementedError()

    override suspend fun putJson(
        url: String,
        body: Any,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = throw NotImplementedError()

    override suspend fun putRaw(
        url: String,
        body: Any,
        contentType: String,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = throw NotImplementedError()

    override suspend fun deleteJson(
        url: String,
        body: Any?,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = throw NotImplementedError()

    override suspend fun deleteRaw(
        url: String,
        body: Any?,
        contentType: String,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = throw NotImplementedError()

    override suspend fun postForm(
        url: String,
        formParams: Map<String, String?>,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = throw NotImplementedError()

    override suspend fun postMultipart(
        url: String,
        form: MultipartForm,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = throw NotImplementedError()
}

class HttpRequestExecutorPatchDefaultTest {

    @Test
    fun patchJson_defaultImplementation_throwsUnsupportedOperationException() = runTest {
        val executor = BareHttpRequestExecutor()

        assertFailsWith<UnsupportedOperationException> {
            executor.patchJson(url = "https://api.example.com/test", body = "{}")
        }
    }

    @Test
    fun patchRaw_defaultImplementation_throwsUnsupportedOperationException() = runTest {
        val executor = BareHttpRequestExecutor()

        assertFailsWith<UnsupportedOperationException> {
            executor.patchRaw(url = "https://api.example.com/test", body = "raw-bytes")
        }
    }
}
