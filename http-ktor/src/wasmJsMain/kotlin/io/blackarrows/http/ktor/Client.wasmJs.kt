package io.blackarrows.http.ktor

import io.blackarrows.http.LenientJson
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

actual fun createHttpClient(): HttpClient =
    HttpClient(Js) {
        install(ContentNegotiation) {
            json(LenientJson)
        }
    }
