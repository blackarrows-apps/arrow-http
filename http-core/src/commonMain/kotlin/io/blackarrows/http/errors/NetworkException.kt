package io.blackarrows.http.errors

/**
 * Exception thrown when a network-level failure occurs.
 *
 * This exception is typically used for transient network errors such as:
 * - Connection timeouts
 * - DNS resolution failures
 * - Network unreachable errors
 * - Socket errors
 *
 * These errors are generally retryable, and the [io.blackarrows.http.io.interceptors.RetryPolicy]
 * can automatically retry requests that throw this exception.
 *
 * @property message A description of the network failure
 * @property cause The underlying network error, if available
 */
class NetworkException(
    message: String,
    cause: Throwable? = null
) : HttpException(message, cause)
