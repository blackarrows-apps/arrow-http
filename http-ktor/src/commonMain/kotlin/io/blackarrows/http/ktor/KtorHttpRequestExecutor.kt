package io.blackarrows.http.ktor

import io.blackarrows.http.HttpHeaders
import io.blackarrows.http.HttpRequestConfig
import io.blackarrows.http.data.MultipartForm
import io.blackarrows.http.io.ApiResponse
import io.blackarrows.http.io.HttpRequestExecutor
import io.blackarrows.http.io.interceptors.HttpPolicy
import io.blackarrows.http.providers.HeaderProvider
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders as KtorHttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.contentType
import kotlinx.serialization.InternalSerializationApi

@OptIn(InternalSerializationApi::class)
class KtorHttpRequestExecutor(
    private val client: HttpClient,
    private val authHeaderProvider: HeaderProvider,
    private val policies: List<HttpPolicy> = emptyList(),
) : HttpRequestExecutor {

    override suspend fun getJson(
        url: String,
        queryParams: Map<String, String>,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse =
        executeWithPolicies {
            val mergedHeaders = mergeHeaders(authRequired, headers, config.headers)
            client.get(url) {
                contentType(ContentType.Application.Json)
                applyHeaders(mergedHeaders)
                url {
                    queryParams.forEach { (k, v) -> parameters.append(k, v) }
                    config.queryParams.forEach { (k, v) -> parameters.append(k, v) }
                }
                applyConfig(config)
            }
        }

    override suspend fun getRaw(
        url: String,
        headers: HttpHeaders,
        queryParams: Map<String, String>,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse =
        executeWithPolicies {
            val mergedHeaders = mergeHeaders(authRequired, headers, config.headers)
            client.get(url) {
                applyHeaders(mergedHeaders)
                url {
                    queryParams.forEach { (k, v) -> parameters.append(k, v) }
                    config.queryParams.forEach { (k, v) -> parameters.append(k, v) }
                }
                applyConfig(config)
            }
        }

    override suspend fun postJson(
        url: String,
        body: Any,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse =
        executeWithPolicies {
            val mergedHeaders = mergeHeaders(authRequired, headers, config.headers)
            client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(body)
                applyHeaders(mergedHeaders)
                applyConfig(config)
            }
        }

    override suspend fun postRaw(
        url: String,
        body: Any,
        contentType: String,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse =
        executeWithPolicies {
            val mergedHeaders = mergeHeaders(authRequired, headers, config.headers)
            client.post(url) {
                this.contentType(ContentType.parse(contentType))
                setBody(body)
                applyHeaders(mergedHeaders)
                applyConfig(config)
            }
        }

    override suspend fun postQuery(
        url: String,
        queryParams: Map<String, String>,
        contentType: String?,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse =
        executeWithPolicies {
            val mergedHeaders = mergeHeaders(authRequired, headers, config.headers)
            client.post(url) {
                contentType?.let { this.contentType(ContentType.parse(it)) }
                applyHeaders(mergedHeaders)
                url {
                    queryParams.forEach { (k, v) -> parameters.append(k, v) }
                    config.queryParams.forEach { (k, v) -> parameters.append(k, v) }
                }
                applyConfig(config)
            }
        }

    override suspend fun putJson(
        url: String,
        body: Any,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse =
        executeWithPolicies {
            val mergedHeaders = mergeHeaders(authRequired, headers, config.headers)
            client.put(url) {
                contentType(ContentType.Application.Json)
                setBody(body)
                applyHeaders(mergedHeaders)
                applyConfig(config)
            }
        }

    override suspend fun putRaw(
        url: String,
        body: Any,
        contentType: String,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse =
        executeWithPolicies {
            val mergedHeaders = mergeHeaders(authRequired, headers, config.headers)
            client.put(url) {
                this.contentType(ContentType.parse(contentType))
                setBody(body)
                applyHeaders(mergedHeaders)
                applyConfig(config)
            }
        }

    override suspend fun deleteJson(
        url: String,
        body: Any?,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse =
        executeWithPolicies {
            val mergedHeaders = mergeHeaders(authRequired, headers, config.headers)
            client.delete(url) {
                contentType(ContentType.Application.Json)
                body?.let { setBody(it) }
                applyHeaders(mergedHeaders)
                applyConfig(config)
            }
        }

    override suspend fun deleteRaw(
        url: String,
        body: Any?,
        contentType: String,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse =
        executeWithPolicies {
            val mergedHeaders = mergeHeaders(authRequired, headers, config.headers)
            client.delete(url) {
                this.contentType(ContentType.parse(contentType))
                body?.let { setBody(it) }
                applyHeaders(mergedHeaders)
                applyConfig(config)
            }
        }

    override suspend fun postForm(
        url: String,
        formParams: Map<String, String?>,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse =
        executeWithPolicies {
            val mergedHeaders = mergeHeaders(authRequired, headers, config.headers)
            client.submitForm(
                url = url,
                formParameters =
                    Parameters.build {
                        formParams.forEach { (k, v) -> if (v != null) append(k, v) }
                    },
            ) {
                applyHeaders(mergedHeaders)
                contentType(ContentType.Application.FormUrlEncoded)
                applyConfig(config)
            }
        }

    override suspend fun postMultipart(
        url: String,
        form: MultipartForm,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig,
    ): ApiResponse =
        executeWithPolicies {
            val mergedHeaders = mergeHeaders(authRequired, headers, config.headers)
            val multipartData =
                formData {
                    form.fields.forEach { (key, value) -> append(key, value) }
                    form.files.forEach { part ->
                        append(
                            key = part.name,
                            value = part.value,
                            headers =
                                Headers.build {
                                    append(KtorHttpHeaders.ContentType, part.contentType ?: "application/octet-stream")
                                    part.filename?.let {
                                        append(
                                            KtorHttpHeaders.ContentDisposition,
                                            "form-data; name=\"${part.name}\"; filename=\"$it\"",
                                        )
                                    }
                                },
                        )
                    }
                }

            client.post(url) {
                applyHeaders(mergedHeaders)
                setBody(MultiPartFormDataContent(multipartData))
                applyConfig(config)
            }
        }

    // --- Helper methods ------------------------------------------------------

    private suspend fun mergeHeaders(
        authRequired: Boolean,
        requestHeaders: HttpHeaders,
        configHeaders: HttpHeaders
    ): Map<String, String> {
        val authHeaders = if (authRequired) authHeaderProvider.getHeaders() else emptyMap()
        return authHeaders + requestHeaders.toMap() + configHeaders.toMap()
    }

    private fun io.ktor.client.request.HttpRequestBuilder.applyHeaders(headers: Map<String, String>) {
        headers.forEach { (k, v) -> header(k, v) }
    }

    private fun io.ktor.client.request.HttpRequestBuilder.applyConfig(config: HttpRequestConfig) {
        config.contentType?.let { contentType(ContentType.parse(it)) }
        // Timeout configuration - can be extended via config.extras if needed
        // Note: Timeouts are typically configured globally on HttpClient
        // followRedirects is typically set on the HttpClient itself, not per-request
        // config.extras can be used for Ktor-specific configuration in the future
    }

    // --- Core policy chain ---------------------------------------------------

    private suspend fun executeWithPolicies(block: suspend () -> HttpResponse): ApiResponse {
        // Build chain in reverse so the first policy wraps the rest
        var chain: suspend () -> ApiResponse = { block().toApiResponse() }

        for (policy in policies.asReversed()) {
            val next = chain
            chain = { policy.intercept(next) }
        }

        return chain()
    }
}
