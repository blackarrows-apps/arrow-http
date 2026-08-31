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
        // Without this, Ktor's OkHttp engine falls back to OkHttp's bare 10s read timeout on
        // every request -- too short for an endpoint that does real synchronous work server-side
        // (e.g. a legacy Firestore->Postgres migration importing years of history), and fatal
        // during a Cloud Run cold start where that same call can take 20-40s+. A silent timeout
        // there doesn't surface as an error: callers on this codebase's legacy-migration path
        // catch and swallow it as "not a legacy account" and fall through to a normal sign-in,
        // leaving the abandoned migration to keep running server-side to completion while the
        // client is left with an empty local profile it never retries.
        install(HttpTimeout) {
            this.requestTimeoutMillis = requestTimeoutMillis ?: 30_000
            this.connectTimeoutMillis = connectTimeoutMillis ?: 15_000
            this.socketTimeoutMillis = socketTimeoutMillis ?: 30_000
        }
    }
