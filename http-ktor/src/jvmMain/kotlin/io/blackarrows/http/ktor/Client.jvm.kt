package io.blackarrows.http.ktor

import io.blackarrows.http.LenientJson
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

actual fun createHttpClient(
    requestTimeoutMillis: Long?,
    connectTimeoutMillis: Long?,
    socketTimeoutMillis: Long?,
): HttpClient =
    HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(LenientJson)
        }
        // See Client.android.kt's matching install(HttpTimeout) block for why this exists.
        install(HttpTimeout) {
            this.requestTimeoutMillis = requestTimeoutMillis ?: 30_000
            this.connectTimeoutMillis = connectTimeoutMillis ?: 15_000
            this.socketTimeoutMillis = socketTimeoutMillis ?: 30_000
        }
    }
