package io.blackarrows.http.io.interceptors

import io.blackarrows.http.errors.AuthException
import io.blackarrows.http.io.ApiResponse
import io.blackarrows.http.providers.HeaderProvider
import kotlinx.coroutines.delay

class AuthPolicy(
    private val authRefresher: AuthRefresher,
    private val headerProvider: HeaderProvider,
    private val maxReauthRetries: Int = 1,
) : HttpPolicy {
    override suspend fun intercept(execute: suspend () -> ApiResponse): ApiResponse {
        var response = execute()
        if (response.statusCode == 401) {
            repeat(maxReauthRetries) { attempt ->
                val result = authRefresher.refreshToken()
                if (result.isSuccess) {
                    // 👇 Force header refresh for next request
                    headerProvider.invalidate()

                    // 👇 Re-execute the request by rebuilding its headers
                    response = execute()
                    if (response.statusCode != 401) return response
                }
                delay(500L * (attempt + 1))
            }

            throw AuthException("Reauthentication failed after $maxReauthRetries attempts.")
        }
        return response
    }
}
