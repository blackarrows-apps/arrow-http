package io.blackarrows.http.ktor.di

import io.blackarrows.http.io.HttpRequestExecutor
import io.blackarrows.http.io.interceptors.AuthPolicy
import io.blackarrows.http.io.interceptors.RetryPolicy
import io.blackarrows.http.ktor.KtorHttpRequestExecutor
import io.blackarrows.http.ktor.NetworkClient
import io.ktor.client.HttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val AUTH_CLIENT = "authClient"
const val DEFAULT_CLIENT = "defaultClient"

const val AUTH_HEADER_PROVIDER = "authHeaderProvider"
const val SYNC_HEADER_PROVIDER = "syncHeaderProvider"

val httpModule =
    module {
        single<HttpRequestExecutor> {
            KtorHttpRequestExecutor(
                client = get(named(DEFAULT_CLIENT)),
                policies =
                    listOf(
                        RetryPolicy(),
                        AuthPolicy(get(), get(named(AUTH_HEADER_PROVIDER))),
                    ),
                authHeaderProvider = get(named(AUTH_HEADER_PROVIDER)),
            )
        }
        single<HttpClient>(named(DEFAULT_CLIENT)) { NetworkClient.httpClient }
        single<HttpClient>(named(AUTH_CLIENT)) { NetworkClient.httpClient }
    }
