package io.blackarrows.http

data class HttpRequestConfig(
    val headers: HttpHeaders = HttpHeaders.Empty,
    val queryParams: Map<String, String> = emptyMap(),
    val contentType: String? = null,
    val timeout: Long? = null,
    val followRedirects: Boolean = true,
    val extras: Map<String, Any> = emptyMap()
) {
    companion object {
        val Default = HttpRequestConfig()
    }
}
