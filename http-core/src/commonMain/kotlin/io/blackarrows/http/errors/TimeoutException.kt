package io.blackarrows.http.errors

/**
 * Exception thrown when an HTTP request times out.
 *
 * This exception is thrown when a request exceeds the configured timeout duration.
 * Timeouts can occur at different stages:
 * - Connection timeout: Failed to establish connection within the timeout period
 * - Request timeout: The entire request took longer than allowed
 * - Socket timeout: No data received within the timeout period
 *
 * @property message A description of the timeout
 * @property cause The underlying timeout error, if available
 */
class TimeoutException(
    message: String = "Request timed out",
    cause: Throwable? = null
) : HttpException(message, cause)
