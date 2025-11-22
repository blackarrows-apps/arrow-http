package io.blackarrows.http.io.interceptors

interface AuthRefresher {
    suspend fun refreshToken(): Result<String>
}
