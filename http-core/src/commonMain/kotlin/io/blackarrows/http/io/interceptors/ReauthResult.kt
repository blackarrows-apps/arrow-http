package io.blackarrows.http.io.interceptors

sealed interface ReauthResult {
    object Success : ReauthResult

    data class NetworkFailure(
        val cause: Throwable?,
    ) : ReauthResult

    data class AuthFailure(
        val cause: Throwable?,
    ) : ReauthResult
}
