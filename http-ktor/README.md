# HTTP Ktor Module

The `http-ktor` module provides a Ktor-based implementation of the `http-core` abstractions. It offers a production-ready HTTP client for Kotlin Multiplatform projects with platform-specific optimizations.

## Overview

This module implements the `HttpRequestExecutor` interface using Ktor Client, providing:

- **Platform-Specific Engines**: OkHttp for Android/JVM, Darwin for iOS
- **JSON Support**: Automatic serialization/deserialization with kotlinx.serialization
- **Content Negotiation**: Built-in content negotiation for JSON APIs
- **Logging**: Configurable request/response logging
- **Koin Integration**: Dependency injection module for easy setup

## Supported Platforms

- Android (OkHttp engine)
- JVM (OkHttp engine)
- iOS (Darwin engine)
  - arm64 (physical devices)
  - x64 (Intel simulators)
  - simulatorArm64 (Apple Silicon simulators)

## Installation

Add the dependency to your project:

```kotlin
// For Kotlin Multiplatform projects
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation("io.blackarrows:http-core:1.0.0")
                implementation("io.blackarrows:http-ktor:1.0.0")
            }
        }
    }
}

// For Android/JVM projects
dependencies {
    implementation("io.blackarrows:http-core:1.0.0")
    implementation("io.blackarrows:http-ktor:1.0.0")
}
```

## Quick Start

### 1. Create HttpClient

The module provides platform-specific HttpClient factories:

```kotlin
import io.blackarrows.http.ktor.createHttpClient

// Basic setup
val httpClient = createHttpClient()

// With configuration
val httpClient = createHttpClient {
    // Connection timeout
    engine {
        connectTimeout = 30_000
        socketTimeout = 30_000
    }

    // Logging
    install(Logging) {
        level = LogLevel.BODY
    }

    // Custom configuration
    defaultRequest {
        url("https://api.example.com/")
        header("User-Agent", "MyApp/1.0")
    }
}
```

### 2. Setup with Koin

The module includes a Koin module for dependency injection:

```kotlin
import io.blackarrows.http.ktor.di.httpKtorModule
import io.blackarrows.http.providers.HeaderProvider
import org.koin.core.context.startKoin
import org.koin.dsl.module

startKoin {
    modules(
        // Your app module
        module {
            single<HeaderProvider> {
                TokenHeaderProvider(get())
            }
        },
        // HTTP Ktor module
        httpKtorModule
    )
}

// Inject HttpRequestExecutor
class ApiRepository(
    private val httpExecutor: HttpRequestExecutor
) {
    suspend fun getUsers(): List<User> {
        val response = httpExecutor.getJson("users")
        return response.bodyAsJson(ListSerializer(User.serializer()))
    }
}
```

### 3. Manual Setup

If you're not using Koin:

```kotlin
import io.blackarrows.http.ktor.KtorHttpRequestExecutor
import io.blackarrows.http.ktor.createHttpClient
import io.blackarrows.http.providers.HeaderProvider
import io.blackarrows.http.io.interceptors.RetryPolicy
import io.blackarrows.http.io.interceptors.AuthPolicy

val httpClient = createHttpClient()

val headerProvider = object : HeaderProvider {
    override suspend fun getHeaders(vararg additional: Pair<String, String>): Map<String, String> {
        return mapOf(
            "Authorization" to "Bearer ${getToken()}",
            *additional
        )
    }
}

val executor = KtorHttpRequestExecutor(
    client = httpClient,
    authHeaderProvider = headerProvider,
    policies = listOf(
        AuthPolicy(authRefresher = myAuthRefresher),
        RetryPolicy(maxRetries = 3)
    )
)
```

## Usage Examples

### GET Requests

```kotlin
// JSON GET
val response = executor.getJson(
    url = "https://api.example.com/users",
    queryParams = mapOf("page" to "1", "limit" to "10"),
    authRequired = true
)
val users = response.bodyAsJson(ListSerializer(User.serializer()))

// Raw GET
val response = executor.getRaw(
    url = "https://api.example.com/file",
    headers = HttpHeaders.of("Accept" to "application/pdf")
)
val pdfBytes = response.body.encodeToByteArray()
```

### POST Requests

```kotlin
// JSON POST
val newUser = User(name = "John", email = "john@example.com")
val response = executor.postJson(
    url = "https://api.example.com/users",
    body = newUser,
    authRequired = true
)

// Form POST
val response = executor.postForm(
    url = "https://api.example.com/login",
    formParams = mapOf(
        "username" to "john",
        "password" to "secret"
    )
)

// Multipart POST (file upload)
val imageBytes = loadImageBytes()
val form = MultipartForm(
    fields = mapOf("description" to "Profile picture"),
    files = listOf(
        MultipartPart(
            name = "avatar",
            value = imageBytes,
            filename = "avatar.jpg",
            contentType = "image/jpeg"
        )
    )
)
val response = executor.postMultipart(
    url = "https://api.example.com/upload",
    form = form,
    authRequired = true
)
```

### PUT Requests

```kotlin
// JSON PUT
val updatedUser = User(id = 1, name = "John Updated")
val response = executor.putJson(
    url = "https://api.example.com/users/1",
    body = updatedUser,
    authRequired = true
)

// Raw PUT
val response = executor.putRaw(
    url = "https://api.example.com/document",
    body = xmlContent,
    contentType = "application/xml",
    authRequired = true
)
```

### DELETE Requests

```kotlin
// DELETE without body
val response = executor.deleteJson(
    url = "https://api.example.com/users/1",
    authRequired = true
)

// DELETE with body
val response = executor.deleteJson(
    url = "https://api.example.com/users/bulk",
    body = listOf(1, 2, 3),
    authRequired = true
)
```

### Request Configuration

```kotlin
val config = HttpRequestConfig(
    headers = HttpHeaders.of("X-Request-ID" to "12345"),
    queryParams = mapOf("debug" to "true"),
    timeout = 60_000L,
    followRedirects = false
)

val response = executor.getJson(
    url = "https://api.example.com/data",
    config = config
)
```

## Platform-Specific Details

### Android

Uses OkHttp engine with Android-specific optimizations:

```kotlin
// In http-ktor/src/androidMain/kotlin/io/blackarrows/http/ktor/Client.android.kt
actual fun createHttpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient {
    return HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        config()
    }
}
```

### iOS

Uses Darwin engine for native iOS networking:

```kotlin
// In http-ktor/src/iosMain/kotlin/io/blackarrows/http/ktor/Client.ios.kt
actual fun createHttpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient {
    return HttpClient(Darwin) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        config()
    }
}
```

### JVM

Uses OkHttp engine for JVM applications:

```kotlin
// In http-ktor/src/jvmMain/kotlin/io/blackarrows/http/ktor/Client.jvm.kt
actual fun createHttpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient {
    return HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        config()
    }
}
```

## Components

### KtorHttpRequestExecutor

Main implementation class:

```kotlin
class KtorHttpRequestExecutor(
    private val client: HttpClient,
    private val authHeaderProvider: HeaderProvider,
    private val policies: List<HttpPolicy> = emptyList()
) : HttpRequestExecutor
```

Features:
- Implements all HTTP methods from `HttpRequestExecutor`
- Automatic header merging (auth + request + config headers)
- Policy chain execution for interceptors
- JSON serialization/deserialization
- Multipart form data support

### KtorApiResponse

Ktor-specific `ApiResponse` implementation:

```kotlin
class KtorApiResponse(
    private val response: HttpResponse
) : ApiResponse {
    override val statusCode: Int
    override val headers: Map<String, List<String>>
    override val body: String

    override fun isSuccessful(): Boolean
    override suspend fun <T> bodyAsJson(deserializer: DeserializationStrategy<T>): T
}
```

### ApiResponseMapper

Extension function to convert Ktor responses:

```kotlin
suspend fun HttpResponse.toApiResponse(): ApiResponse
```

## Error Mapping

The module automatically maps Ktor exceptions to http-core exception types:

```kotlin
try {
    val response = client.get(url)
    response.toApiResponse()
} catch (e: Exception) {
    when (e) {
        is kotlinx.serialization.SerializationException ->
            throw SerializationException("JSON parsing failed", e, ErrorCodes.SER_DESERIALIZATION_FAILED)

        is io.ktor.client.network.sockets.ConnectTimeoutException ->
            throw TimeoutException("Connection timeout", cause = e, errorCode = ErrorCodes.NET_CONNECTION_TIMEOUT)

        is io.ktor.client.network.sockets.SocketTimeoutException ->
            throw TimeoutException("Socket timeout", cause = e, errorCode = ErrorCodes.NET_REQUEST_TIMEOUT)

        else ->
            throw NetworkException("Network error", e, isRetryable = true)
    }
}
```

## Configuration

### Timeouts

Configure timeouts globally on the HttpClient:

```kotlin
val httpClient = createHttpClient {
    install(HttpTimeout) {
        requestTimeoutMillis = 60_000
        connectTimeoutMillis = 30_000
        socketTimeoutMillis = 30_000
    }
}
```

### Logging

Enable request/response logging:

```kotlin
val httpClient = createHttpClient {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.BODY
        filter { request ->
            !request.url.toString().contains("sensitive")
        }
    }
}
```

### Custom Headers

Add default headers:

```kotlin
val httpClient = createHttpClient {
    defaultRequest {
        header("User-Agent", "MyApp/1.0.0")
        header("Accept-Language", "en-US")
    }
}
```

### SSL/TLS Configuration (JVM/Android)

```kotlin
val httpClient = createHttpClient {
    engine {
        config {
            sslSocketFactory(customSSLSocketFactory, customTrustManager)
            hostnameVerifier(customHostnameVerifier)
        }
    }
}
```

## Koin Module

The provided Koin module sets up everything you need:

```kotlin
val httpKtorModule = module {
    single {
        createHttpClient()
    }

    single<HttpRequestExecutor> {
        KtorHttpRequestExecutor(
            client = get(),
            authHeaderProvider = get(),
            policies = emptyList()
        )
    }
}
```

To customize:

```kotlin
val customHttpModule = module {
    single {
        createHttpClient {
            install(HttpTimeout) {
                requestTimeoutMillis = 60_000
            }
        }
    }

    single<HttpRequestExecutor> {
        KtorHttpRequestExecutor(
            client = get(),
            authHeaderProvider = get(),
            policies = listOf(
                AuthPolicy(get()),
                RetryPolicy(maxRetries = 3)
            )
        )
    }
}
```

## Testing

Example test setup with MockEngine:

```kotlin
@Test
fun testApiCall() = runTest {
    val mockEngine = MockEngine { request ->
        respond(
            content = ByteReadChannel("""{"id": 1, "name": "Test"}"""),
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    }

    val client = HttpClient(mockEngine) {
        install(ContentNegotiation) {
            json()
        }
    }

    val executor = KtorHttpRequestExecutor(
        client = client,
        authHeaderProvider = MockHeaderProvider(),
        policies = emptyList()
    )

    val response = executor.getJson("https://api.example.com/test")
    assertEquals(200, response.statusCode)
}
```

## Dependencies

```kotlin
dependencies {
    // Core
    api(project(":http-core"))

    // Ktor
    implementation("io.ktor:ktor-client-core:3.0.2")
    implementation("io.ktor:ktor-client-content-negotiation:3.0.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.2")
    implementation("io.ktor:ktor-client-logging:3.0.2")

    // Platform-specific engines
    // - Android/JVM: io.ktor:ktor-client-okhttp
    // - iOS: io.ktor:ktor-client-darwin

    // Koin
    implementation("io.insert-koin:koin-core:4.0.0")
}
```

## Migration from Direct Ktor Usage

If you're currently using Ktor directly:

**Before:**
```kotlin
val client = HttpClient(OkHttp) { /* config */ }
val response: HttpResponse = client.get("https://api.example.com/users")
val users: List<User> = response.body()
```

**After:**
```kotlin
val executor: HttpRequestExecutor = get() // or create manually
val response: ApiResponse = executor.getJson("https://api.example.com/users")
val users: List<User> = response.bodyAsJson(ListSerializer(User.serializer()))
```

Benefits:
- Client-agnostic code
- Built-in retry and auth handling
- Better error handling
- Easier to test

## License

This module is part of the arrow-http project.
