package io.blackarrows.http.test

import io.blackarrows.http.HttpHeaders
import io.blackarrows.http.HttpRequestConfig
import io.blackarrows.http.data.MultipartForm
import io.blackarrows.http.io.ApiResponse
import io.blackarrows.http.io.HttpRequestExecutor

/**
 * A test double for [HttpRequestExecutor].
 *
 * Stubs responses and exceptions per URL. Records every call made so tests can
 * assert that the correct requests were sent.
 *
 * ## Basic usage
 * ```kotlin
 * val fake = FakeHttpRequestExecutor()
 * fake.stub("https://api.example.com/config", FakeApiResponse.success("""{"key":"value"}"""))
 *
 * val result = myDataSource.fetch() // internally calls fake
 *
 * assertEquals(1, fake.calls.size)
 * assertEquals("https://api.example.com/config", fake.calls.first().url)
 * ```
 *
 * ## Stubbing errors
 * ```kotlin
 * fake.stub("https://api.example.com/config", FakeApiResponse.error(404))
 *
 * // or throw an exception:
 * fake.stubException("https://api.example.com/config", NetworkException("timeout"))
 * ```
 *
 * ## Default response
 * If no stub matches the URL, [defaultResponse] is returned (200 empty body by default).
 * Override to change the fallback:
 * ```kotlin
 * val fake = FakeHttpRequestExecutor(defaultResponse = FakeApiResponse.error(500))
 * ```
 */
class FakeHttpRequestExecutor(
    private val defaultResponse: ApiResponse = FakeApiResponse.success(),
) : HttpRequestExecutor {

    private val stubs = mutableMapOf<String, ApiResponse>()
    private val exceptions = mutableMapOf<String, Throwable>()
    private val _calls = mutableListOf<RecordedCall>()

    /** All calls made to this executor, in order. */
    val calls: List<RecordedCall> get() = _calls

    /** Stub a URL to return a specific [ApiResponse]. */
    fun stub(url: String, response: ApiResponse) {
        stubs[url] = response
    }

    /** Stub a URL to throw an exception (e.g. [io.blackarrows.http.errors.NetworkException]). */
    fun stubException(url: String, exception: Throwable) {
        exceptions[url] = exception
    }

    /** Clear all stubs, exceptions, and recorded calls. */
    fun reset() {
        stubs.clear()
        exceptions.clear()
        _calls.clear()
    }

    private fun handle(url: String, call: RecordedCall): ApiResponse {
        _calls.add(call)
        exceptions[url]?.let { throw it }
        return stubs[url] ?: defaultResponse
    }

    override suspend fun getJson(
        url: String,
        queryParams: Map<String, String>,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = handle(url, RecordedCall(FakeHttpMethod.GET, url, queryParams = queryParams))

    override suspend fun getRaw(
        url: String,
        headers: HttpHeaders,
        queryParams: Map<String, String>,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = handle(url, RecordedCall(FakeHttpMethod.GET, url, queryParams = queryParams))

    override suspend fun postJson(
        url: String,
        body: Any,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = handle(url, RecordedCall(FakeHttpMethod.POST, url, body = body))

    override suspend fun postRaw(
        url: String,
        body: Any,
        contentType: String,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = handle(url, RecordedCall(FakeHttpMethod.POST, url, body = body))

    override suspend fun postForm(
        url: String,
        formParams: Map<String, String?>,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = handle(url, RecordedCall(FakeHttpMethod.POST, url, body = formParams))

    override suspend fun postQuery(
        url: String,
        queryParams: Map<String, String>,
        contentType: String?,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = handle(url, RecordedCall(FakeHttpMethod.POST, url, queryParams = queryParams))

    override suspend fun postMultipart(
        url: String,
        form: MultipartForm,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = handle(url, RecordedCall(FakeHttpMethod.POST, url, body = form))

    override suspend fun putJson(
        url: String,
        body: Any,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = handle(url, RecordedCall(FakeHttpMethod.PUT, url, body = body))

    override suspend fun putRaw(
        url: String,
        body: Any,
        contentType: String,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = handle(url, RecordedCall(FakeHttpMethod.PUT, url, body = body))

    override suspend fun deleteJson(
        url: String,
        body: Any?,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = handle(url, RecordedCall(FakeHttpMethod.DELETE, url, body = body))

    override suspend fun deleteRaw(
        url: String,
        body: Any?,
        contentType: String,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse = handle(url, RecordedCall(FakeHttpMethod.DELETE, url, body = body))
}
