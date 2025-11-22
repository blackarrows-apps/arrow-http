package io.blackarrows.http.errors

import io.blackarrows.http.io.ApiResponse

/**
 * Exception thrown when an HTTP request completes but returns an error status code.
 *
 * This exception wraps HTTP error responses (4xx and 5xx status codes) and provides
 * access to the full response data including status code, headers, and body.
 *
 * Common HTTP status codes:
 * - 400 Bad Request
 * - 403 Forbidden
 * - 404 Not Found
 * - 409 Conflict
 * - 429 Too Many Requests
 * - 500 Internal Server Error
 * - 502 Bad Gateway
 * - 503 Service Unavailable
 *
 * @property statusCode The HTTP status code of the error response
 * @property response The full API response containing headers and body
 * @property message A description of the HTTP error
 */
class HttpStatusException(
    val statusCode: Int,
    val response: ApiResponse,
    message: String = "HTTP $statusCode error"
) : HttpException(message)
