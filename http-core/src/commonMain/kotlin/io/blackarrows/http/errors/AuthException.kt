package io.blackarrows.http.errors

/**
 * Exception thrown when authentication or authorization fails.
 *
 * This exception is used for authentication-related failures such as:
 * - Token refresh failures
 * - Invalid credentials
 * - Expired tokens that cannot be refreshed
 * - Insufficient permissions
 * - Maximum reauthentication retry attempts exceeded
 *
 * The [io.blackarrows.http.io.interceptors.AuthPolicy] throws this exception when
 * it fails to reauthenticate after the configured number of retry attempts.
 *
 * @property message A description of the authentication failure
 * @property cause The underlying authentication error, if available
 */
class AuthException(
    message: String,
    cause: Throwable? = null
) : HttpException(message, cause)
