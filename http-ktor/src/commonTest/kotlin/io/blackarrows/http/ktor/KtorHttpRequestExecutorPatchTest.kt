package io.blackarrows.http.ktor

import io.blackarrows.http.providers.HeaderProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

private object NoopHeaderProvider : HeaderProvider {
    override suspend fun getHeaders(vararg additional: Pair<String, String>): Map<String, String> = emptyMap()
    override fun invalidate() {}
}

class KtorHttpRequestExecutorPatchTest {

    private fun executorWithMock(onRequest: (io.ktor.client.request.HttpRequestData) -> Unit): KtorHttpRequestExecutor {
        val mockEngine = MockEngine { request ->
            onRequest(request)
            respond(
                content = ByteReadChannel("""{"id": 1}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(io.ktor.http.HttpHeaders.ContentType, "application/json"),
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) { json() }
        }

        return KtorHttpRequestExecutor(
            client = client,
            authHeaderProvider = NoopHeaderProvider,
            policies = emptyList(),
        )
    }

    @Test
    fun patchJson_issuesActualPatchRequest() = runTest {
        var capturedMethod: HttpMethod? = null
        val executor = executorWithMock { request -> capturedMethod = request.method }

        val response = executor.patchJson(
            url = "https://api.example.com/test",
            body = mapOf("title" to "updated"),
        )

        assertEquals(HttpMethod.Patch, capturedMethod)
        assertEquals(200, response.statusCode)
    }

    @Test
    fun patchRaw_issuesActualPatchRequest() = runTest {
        var capturedMethod: HttpMethod? = null
        val executor = executorWithMock { request -> capturedMethod = request.method }

        val response = executor.patchRaw(
            url = "https://api.example.com/test",
            body = "raw-bytes",
            contentType = "text/plain",
        )

        assertEquals(HttpMethod.Patch, capturedMethod)
        assertEquals(200, response.statusCode)
    }
}
