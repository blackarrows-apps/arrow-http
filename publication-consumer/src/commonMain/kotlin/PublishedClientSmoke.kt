import io.blackarrows.http.io.HttpRequestExecutor
import io.blackarrows.http.ktor.KtorHttpRequestExecutor
import io.blackarrows.http.ktor.createHttpClient
import io.blackarrows.http.providers.HeaderProvider
import io.ktor.client.HttpClient

private object EmptyHeaderProvider : HeaderProvider {
    override suspend fun getHeaders(
        vararg additional: Pair<String, String>,
    ): Map<String, String> = additional.toMap()

    override fun invalidate() = Unit
}

/** Representative public API compiled only from the configured Maven repository. */
fun constructPublishedClient(): HttpClient = createHttpClient()

/** Proves the published Ktor implementation can be supplied through the core abstraction. */
fun constructPublishedExecutor(client: HttpClient): HttpRequestExecutor =
    KtorHttpRequestExecutor(
        client = client,
        authHeaderProvider = EmptyHeaderProvider,
    )
