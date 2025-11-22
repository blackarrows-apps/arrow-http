package io.blackarrows.http.io.interceptors

import io.blackarrows.http.io.ApiResponse

interface HttpPolicy {
    suspend fun intercept(execute: suspend () -> ApiResponse): ApiResponse
}
