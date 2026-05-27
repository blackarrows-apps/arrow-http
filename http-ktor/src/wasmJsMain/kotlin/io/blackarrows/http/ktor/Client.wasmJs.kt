package io.blackarrows.http.ktor

import io.blackarrows.http.LenientJson
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.observer.wrap
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json

// @OptIn(InternalAPI::class) cannot be used here: the import path for InternalAPI
// differs across Ktor minor versions and the wasmJs compilation fails to resolve it.
// @Suppress("OPT_IN_USAGE_ERROR") achieves the same result without an import.
@Suppress("OPT_IN_USAGE_ERROR")
actual fun createHttpClient(): HttpClient =
    HttpClient(Js) {
        install(ContentNegotiation) {
            json(LenientJson)
        }
        // Prevent fetch() calls from hanging forever on iOS Safari/WebKit.
        // The Js engine implements timeouts via AbortController, which is
        // supported in all modern browsers including iOS Safari 15+.
        install(HttpTimeout) {
            requestTimeoutMillis = 15_000   // abort if no full response in 15 s
            connectTimeoutMillis = 10_000   // abort if connection not established in 10 s
        }
        // WHY THIS IS NEEDED — browser gzip + Ktor Content-Length check
        //
        // The browser's Fetch API automatically decompresses gzip responses but keeps the
        // original *compressed* Content-Length header intact (e.g. Content-Length: 1025
        // but the actual decompressed body is 1339 bytes).
        //
        // `client.post()` → `HttpStatement.execute()` → `HttpStatement.fetchResponse()`
        // which calls `call.save()` on **every** response (not just errors) to buffer the
        // body in memory. `SavedHttpCall.init` then calls:
        //     checkContentLength(response.contentLength(), responseBody.size, method)
        // This throws "Content-Length mismatch: expected 1025, received 1339" because the
        // compressed header size doesn't match the decompressed body size.
        //
        // FIX: strip the Content-Length header from every response in the receivePipeline
        // so checkContentLength() sees null and exits early without throwing.
        // The response body itself is not affected.
        install(createClientPlugin("StripResponseContentLength") {
            client.receivePipeline.intercept(HttpReceivePipeline.After) { response ->
                if (response.headers[HttpHeaders.ContentLength] == null) return@intercept
                val strippedHeaders = Headers.build {
                    response.headers.forEach { key, values ->
                        if (!key.equals(HttpHeaders.ContentLength, ignoreCase = true)) {
                            values.forEach { value -> append(key, value) }
                        }
                    }
                }
                // rawContent is @InternalAPI — suppressed via function-level annotation above.
                // Pass the current rawContent (wrapped by SaveBodyPlugin's ByteChannelReplay)
                // so save() can still read and buffer the body correctly.
                val newResponse = response.call.wrap(response.rawContent, strippedHeaders).response
                proceedWith(newResponse)
            }
        })
    }
