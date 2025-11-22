# HTTP Core Module

The `http-core` module provides client-agnostic HTTP abstractions for Kotlin Multiplatform projects. It contains no implementation-specific dependencies, allowing you to build applications that can swap HTTP client implementations without changing your business logic.

## Overview

This module defines the core contracts and types needed for HTTP communication across all platforms:

- **Abstractions**: Framework-independent interfaces for HTTP operations
- **Error Handling**: Comprehensive exception hierarchy for type-safe error handling
- **Interceptors**: Policy-based request/response interception system
- **Type Safety**: Strongly-typed configuration and response handling

## Supported Platforms

- Android
- JVM
- iOS (arm64, x64, simulatorArm64)

## Core Components

### HttpRequestExecutor

The main interface for executing HTTP requests. It provides methods for all common HTTP operations:

```kotlin
interface HttpRequestExecutor {
    // GET requests
    suspend fun getJson(url: String, queryParams: Map<String, String> = emptyMap(), ...): ApiResponse
    suspend fun getRaw(url: String, headers: HttpHeaders = HttpHeaders.Empty, ...): ApiResponse

    // POST requests
    suspend fun postJson(url: String, body: Any, ...): ApiResponse
    suspend fun postRaw(url: String, body: Any, contentType: String, ...): ApiResponse
    suspend fun postForm(url: String, formParams: Map<String, String?>, ...): ApiResponse
    suspend fun postMultipart(url: String, form: MultipartForm, ...): ApiResponse
    suspend fun postQuery(url: String, queryParams: Map<String, String>, ...): ApiResponse

    // PUT requests
    suspend fun putJson(url: String, body: Any, ...): ApiResponse
    suspend fun putRaw(url: String, body: Any, contentType: String, ...): ApiResponse

    // DELETE requests
    suspend fun deleteJson(url: String, body: Any? = null, ...): ApiResponse
    suspend fun deleteRaw(url: String, body: Any? = null, contentType: String, ...): ApiResponse
}
```

### HttpHeaders

Framework-independent header container:

```kotlin
data class HttpHeaders(val values: Map<String, String>) {
    operator fun plus(other: HttpHeaders): HttpHeaders
    operator fun plus(pair: Pair<String, String>): HttpHeaders
    fun toMap(): Map<String, String>

    companion object {
        fun of(vararg pairs: Pair<String, String>): HttpHeaders
        val Empty: HttpHeaders
    }
}

// Usage
val headers = HttpHeaders.of(
    "Authorization" to "Bearer token",
    "Content-Type" to "application/json"
)
```

### HttpRequestConfig

Request configuration object:

```kotlin
data class HttpRequestConfig(
    val headers: HttpHeaders = HttpHeaders.Empty,
    val queryParams: Map<String, String> = emptyMap(),
    val contentType: String? = null,
    val timeout: Long? = null,
    val followRedirects: Boolean = true,
    val extras: Map<String, Any> = emptyMap()
)

// Usage
val config = HttpRequestConfig(
    headers = HttpHeaders.of("X-Custom-Header" to "value"),
    timeout = 30_000L,
    followRedirects = false
)
```

### ApiResponse

Type-safe response wrapper:

```kotlin
interface ApiResponse {
    val statusCode: Int
    val headers: Map<String, List<String>>
    val body: String

    fun isSuccessful(): Boolean
    suspend fun <T> bodyAsJson(deserializer: DeserializationStrategy<T>): T
}

// Usage
val response = executor.getJson("https://api.example.com/users")
if (response.isSuccessful()) {
    val users = response.bodyAsJson(ListSerializer(User.serializer()))
}
```

## Error Handling

The module provides a comprehensive exception hierarchy for type-safe error handling:

### HttpException

Base sealed class for all HTTP-related exceptions:

```kotlin
sealed class HttpException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)
```

### Exception Types

#### 1. NetworkException (Retryable)

Network-level failures that can be retried:

```kotlin
class NetworkException(
    message: String,
    cause: Throwable? = null,
    val errorCode: String? = null,
    val isRetryable: Boolean = true
) : HttpException(message, cause)

// Example error codes:
// - NET_CONNECTION_UNAVAILABLE: No network connection
// - NET_CONNECTION_TIMEOUT: Connection timed out
// - NET_DNS_FAILURE: DNS resolution failed
// - NET_SSL_HANDSHAKE_FAILED: SSL/TLS handshake failed
```

#### 2. AuthException

Authentication/authorization failures:

```kotlin
class AuthException(
    message: String,
    val statusCode: Int,
    cause: Throwable? = null,
    val errorCode: String? = null,
    val canRetryWithRefresh: Boolean = false
) : HttpException(message, cause)

// Example error codes:
// - AUTH_TOKEN_EXPIRED: Token has expired
// - AUTH_TOKEN_INVALID: Token is invalid
// - AUTH_REFRESH_FAILED: Token refresh failed
// - AUTH_INSUFFICIENT_PERMISSIONS: Insufficient permissions
```

#### 3. HttpStatusException

HTTP status code errors (4xx, 5xx):

```kotlin
class HttpStatusException(
    message: String,
    val statusCode: Int,
    val responseBody: String? = null,
    cause: Throwable? = null,
    val errorCode: String? = null
) : HttpException(message, cause)

// Handles all HTTP error responses:
// - 400 Bad Request
// - 403 Forbidden
// - 404 Not Found
// - 409 Conflict
// - 429 Too Many Requests
// - 500 Internal Server Error
// - 503 Service Unavailable
```

#### 4. TimeoutException

Request timeout failures:

```kotlin
class TimeoutException(
    message: String,
    val timeoutMillis: Long? = null,
    cause: Throwable? = null,
    val errorCode: String? = null
) : HttpException(message, cause)

// Example error codes:
// - NET_CONNECTION_TIMEOUT: Connection timeout
// - NET_REQUEST_TIMEOUT: Request timeout
```

#### 5. SerializationException

JSON serialization/deserialization errors:

```kotlin
class SerializationException(
    message: String,
    cause: Throwable? = null,
    val errorCode: String? = null
) : HttpException(message, cause)

// Example error codes:
// - SER_SERIALIZATION_FAILED: JSON serialization failed
// - SER_DESERIALIZATION_FAILED: JSON deserialization failed
// - SER_INVALID_JSON: Invalid JSON format
// - SER_TYPE_MISMATCH: Type mismatch during deserialization
```

### ErrorCodes Object

Provides constants for HTTP status codes and custom error codes:

```kotlin
object ErrorCodes {
    // HTTP Status Codes (1xx-5xx)
    const val HTTP_OK = 200
    const val HTTP_CREATED = 201
    const val HTTP_BAD_REQUEST = 400
    const val HTTP_UNAUTHORIZED = 401
    const val HTTP_FORBIDDEN = 403
    const val HTTP_NOT_FOUND = 404
    const val HTTP_TOO_MANY_REQUESTS = 429
    const val HTTP_INTERNAL_SERVER_ERROR = 500
    const val HTTP_SERVICE_UNAVAILABLE = 503

    // Custom Error Codes
    const val AUTH_TOKEN_EXPIRED = "AUTH_001"
    const val NET_CONNECTION_TIMEOUT = "NET_002"
    const val SER_DESERIALIZATION_FAILED = "SER_002"

    // Helper functions
    fun isSuccessful(statusCode: Int): Boolean
    fun isClientError(statusCode: Int): Boolean
    fun isServerError(statusCode: Int): Boolean
    fun isError(statusCode: Int): Boolean
    fun getHttpStatusDescription(statusCode: Int): String
}
```

### Error Handling Example

```kotlin
try {
    val response = executor.getJson("https://api.example.com/data")
    val data = response.bodyAsJson(Data.serializer())
    // Process data
} catch (e: AuthException) {
    when (e.errorCode) {
        ErrorCodes.AUTH_TOKEN_EXPIRED -> {
            if (e.canRetryWithRefresh) {
                // Refresh token and retry
            } else {
                // Redirect to login
            }
        }
        ErrorCodes.AUTH_INSUFFICIENT_PERMISSIONS -> {
            // Show permission denied message
        }
    }
} catch (e: NetworkException) {
    if (e.isRetryable) {
        // Retry with exponential backoff
    } else {
        // Show network error message
    }
} catch (e: HttpStatusException) {
    when (e.statusCode) {
        ErrorCodes.HTTP_NOT_FOUND -> {
            // Handle 404
        }
        ErrorCodes.HTTP_TOO_MANY_REQUESTS -> {
            // Handle rate limiting
        }
        in 500..599 -> {
            // Handle server errors
        }
    }
} catch (e: TimeoutException) {
    // Handle timeout
} catch (e: SerializationException) {
    // Handle JSON parsing errors
}
```

## Interceptors

The module provides a policy-based interceptor system for cross-cutting concerns:

### HttpPolicy

Base interface for interceptors:

```kotlin
interface HttpPolicy {
    suspend fun intercept(next: suspend () -> ApiResponse): ApiResponse
}
```

### AuthPolicy

Handles authentication with automatic token refresh:

```kotlin
class AuthPolicy(
    private val authRefresher: AuthRefresher,
    private val maxRetries: Int = 1
) : HttpPolicy {
    override suspend fun intercept(next: suspend () -> ApiResponse): ApiResponse
}

// Usage
val authPolicy = AuthPolicy(
    authRefresher = object : AuthRefresher {
        override suspend fun refresh(): ReauthResult {
            // Refresh token logic
            return ReauthResult.Success
        }
    },
    maxRetries = 2
)
```

### RetryPolicy

Handles automatic retries for transient failures:

```kotlin
class RetryPolicy(
    private val maxRetries: Int = 3,
    private val initialDelayMs: Long = 1000,
    private val maxDelayMs: Long = 10000,
    private val factor: Double = 2.0
) : HttpPolicy {
    override suspend fun intercept(next: suspend () -> ApiResponse): ApiResponse
}

// Usage
val retryPolicy = RetryPolicy(
    maxRetries = 3,
    initialDelayMs = 500,
    factor = 2.0
)
```

### Composing Policies

Policies are executed in the order they're provided:

```kotlin
val executor = KtorHttpRequestExecutor(
    client = httpClient,
    authHeaderProvider = headerProvider,
    policies = listOf(
        authPolicy,    // Runs first
        retryPolicy    // Runs second
    )
)
```

## Providers

### HeaderProvider

Interface for dynamic header injection:

```kotlin
interface HeaderProvider {
    suspend fun getHeaders(vararg additional: Pair<String, String>): Map<String, String>
}

// Example implementation
class TokenHeaderProvider(private val tokenStore: TokenStore) : HeaderProvider {
    override suspend fun getHeaders(vararg additional: Pair<String, String>): Map<String, String> {
        val token = tokenStore.getAccessToken()
        return mapOf(
            "Authorization" to "Bearer $token",
            *additional
        )
    }
}
```

## Data Types

### MultipartForm

For file uploads:

```kotlin
data class MultipartForm(
    val fields: Map<String, String>,
    val files: List<MultipartPart>
)

data class MultipartPart(
    val name: String,
    val value: ByteArray,
    val filename: String? = null,
    val contentType: String? = null
)

// Usage
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
executor.postMultipart("https://api.example.com/upload", form)
```

### SupportedContentType

Enum for common content types:

```kotlin
enum class SupportedContentType(val value: String) {
    JSON("application/json"),
    FORM_URL_ENCODED("application/x-www-form-urlencoded"),
    MULTIPART_FORM_DATA("multipart/form-data"),
    TEXT_PLAIN("text/plain"),
    TEXT_HTML("text/html"),
    XML("application/xml"),
    OCTET_STREAM("application/octet-stream")
}
```

## Logging

### HttpLogger

Interface for logging HTTP operations:

```kotlin
interface HttpLogger {
    fun logRequest(method: String, url: String, headers: Map<String, String>, body: String?)
    fun logResponse(statusCode: Int, headers: Map<String, List<String>>, body: String)
    fun logError(throwable: Throwable)
}
```

## Integration

This module is designed to be used with implementation modules like `http-ktor`. See the main project README for integration examples.

## Dependencies

```kotlin
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}
```

## License

This module is part of the arrow-http project.
