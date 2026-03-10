package io.blackarrows.http.test

/**
 * Represents a single HTTP call recorded by [FakeHttpRequestExecutor].
 * Use [calls] on the executor to verify that the expected requests were made.
 */
data class RecordedCall(
    val method: FakeHttpMethod,
    val url: String,
    val body: Any? = null,
    val queryParams: Map<String, String> = emptyMap(),
)

enum class FakeHttpMethod { GET, POST, PUT, DELETE }
