package io.blackarrows.http.test

import io.blackarrows.http.io.ApiResponse

/**
 * A simple [ApiResponse] implementation for use in tests.
 *
 * Usage:
 * ```kotlin
 * FakeApiResponse.success("""{"key": "value"}""")
 * FakeApiResponse.error(404)
 * FakeApiResponse.error(500, """{"error": "Internal server error"}""")
 * ```
 */
class FakeApiResponse(
    override val statusCode: Int,
    override val body: ByteArray?,
    override val headers: Map<String, String> = emptyMap(),
) : ApiResponse {

    constructor(
        statusCode: Int,
        body: String?,
        headers: Map<String, String> = emptyMap(),
    ) : this(statusCode, body?.encodeToByteArray(), headers)

    companion object {
        fun success(body: String = "", statusCode: Int = 200): FakeApiResponse =
            FakeApiResponse(statusCode, body)

        fun error(statusCode: Int, body: String = ""): FakeApiResponse =
            FakeApiResponse(statusCode, body)
    }
}
