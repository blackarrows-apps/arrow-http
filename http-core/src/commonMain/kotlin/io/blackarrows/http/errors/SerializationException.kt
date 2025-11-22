package io.blackarrows.http.errors

/**
 * Exception thrown when request/response serialization or deserialization fails.
 *
 * This exception is used when:
 * - Request body cannot be serialized to JSON
 * - Response body cannot be deserialized from JSON
 * - Invalid JSON format in response
 * - Type mismatch during deserialization
 *
 * @property message A description of the serialization error
 * @property cause The underlying serialization error
 */
class SerializationException(
    message: String,
    cause: Throwable? = null
) : HttpException(message, cause)
