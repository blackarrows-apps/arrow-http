package io.blackarrows.http.io.interceptors

import io.blackarrows.http.errors.NetworkException
import io.blackarrows.http.io.ApiResponse
import kotlinx.coroutines.delay

class RetryPolicy(
    private val maxRetries: Int = 3,
    private val delayMs: Long = 500,
) : HttpPolicy {
    override suspend fun intercept(execute: suspend () -> ApiResponse): ApiResponse {
        var lastError: Throwable? = null

        repeat(maxRetries) { attempt ->
            try {
                return execute()
            } catch (e: NetworkException) {
                lastError = e
                delay(delayMs * (attempt + 1))
            }
        }

        throw lastError ?: NetworkException("Network failure after $maxRetries attempts.")
    }
}
