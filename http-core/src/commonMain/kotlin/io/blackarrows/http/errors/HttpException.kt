package io.blackarrows.http.errors

/**
 * Base exception class for all HTTP-related errors in the arrow-http library.
 *
 * This sealed class provides a structured hierarchy for different types of HTTP errors,
 * making it easier to handle specific error cases in a type-safe manner.
 *
 * @property message A human-readable description of the error
 * @property cause The underlying exception that caused this error, if any
 */
sealed class HttpException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)
