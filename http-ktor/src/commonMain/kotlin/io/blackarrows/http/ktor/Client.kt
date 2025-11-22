package io.blackarrows.http.ktor

import io.ktor.client.HttpClient

expect fun createHttpClient(): HttpClient

object NetworkClient {
    val httpClient: HttpClient by lazy {
        createHttpClient()
    }
}
