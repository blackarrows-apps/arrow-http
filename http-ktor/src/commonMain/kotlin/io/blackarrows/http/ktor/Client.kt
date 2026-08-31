package io.blackarrows.http.ktor

import io.ktor.client.HttpClient

/**
 * Creates the platform's Ktor [HttpClient]. Each timeout parameter overrides the
 * platform's built-in default (tuned per engine); leave it `null` to keep that default.
 */
expect fun createHttpClient(
    requestTimeoutMillis: Long? = null,
    connectTimeoutMillis: Long? = null,
    socketTimeoutMillis: Long? = null,
): HttpClient

object NetworkClient {
    val httpClient: HttpClient by lazy {
        createHttpClient()
    }
}
