package io.blackarrows.http.ktor

import io.blackarrows.http.LenientJson
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import platform.Foundation.NSURLRequestReloadIgnoringLocalCacheData

actual fun createHttpClient(
    requestTimeoutMillis: Long?,
    connectTimeoutMillis: Long?,
    socketTimeoutMillis: Long?,
): HttpClient =
    HttpClient(Darwin) {
        install(ContentNegotiation) {
            json(LenientJson)
        }
        // See Client.android.kt's matching install(HttpTimeout) block for why this exists.
        install(HttpTimeout) {
            this.requestTimeoutMillis = requestTimeoutMillis ?: 30_000
            this.connectTimeoutMillis = connectTimeoutMillis ?: 15_000
            this.socketTimeoutMillis = socketTimeoutMillis ?: 30_000
        }
        engine {
            // Client.android.kt's OkHttp engine never caches HTTP responses to disk -- no OkHttp
            // `Cache` is configured, so every request always hits the network. Darwin's default
            // engine config has no equivalent opt-out: it routes requests through NSURLSession's
            // default cache policy, which honors the response's Cache-Control/ETag/Last-Modified
            // (or even RFC 7234 heuristic freshness with no cache headers at all) against the
            // shared, disk-backed NSURLCache -- and that cache survives app relaunches. That
            // silently serves a stale response on iOS for endpoints callers expect to be freshly
            // fetched every call, with no equivalent staleness on Android. Force every request to
            // bypass the cache so both platforms behave the same.
            configureRequest {
                setCachePolicy(NSURLRequestReloadIgnoringLocalCacheData)
            }
        }
    }
