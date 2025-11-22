# Arrow HTTP

A modular, client-agnostic HTTP library for Kotlin Multiplatform projects. Write your HTTP code once, run it everywhere.

**Author:** [Emmanuel Conradie](https://github.com/E5c11)

## Why Arrow HTTP?

- **Client Agnostic**: Write business logic independent of HTTP client implementation
- **Type Safe**: Comprehensive error handling with sealed exception hierarchy
- **Multiplatform**: Android, iOS, and JVM support out of the box
- **Modular**: Use only what you need - separate core abstractions from implementations
- **Production Ready**: Built-in retry policies, auth handling, and request interceptors
- **Extensible**: Currently supports Ktor, with more clients planned based on community demand

## Quick Start

### 1. Add Dependencies

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

// For Kotlin Multiplatform
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation("io.github.blackarrows-apps:http-core:1.0.0")
                implementation("io.github.blackarrows-apps:http-ktor:1.0.0")
            }
        }
    }
}
```

### 2. Setup (with Koin)

```kotlin
import io.blackarrows.http.ktor.di.httpModule
import io.blackarrows.http.providers.HeaderProvider
import io.blackarrows.http.providers.AuthRefresher
import org.koin.core.context.startKoin
import org.koin.dsl.module

startKoin {
    modules(
        module {
            single<HeaderProvider> {
                object : HeaderProvider {
                    override suspend fun getHeaders(vararg additional: Pair<String, String>): Map<String, String> {
                        return mapOf(
                            "Authorization" to "Bearer ${getToken()}",
                            *additional
                        )
                    }

                    override fun invalidate() {
                        // Clear cached headers if needed
                    }
                }
            }

            single<AuthRefresher> {
                object : AuthRefresher {
                    override suspend fun refreshToken(): Result<String> {
                        // Implement token refresh logic
                        return Result.success("new_token")
                    }
                }
            }
        },
        httpModule
    )
}
```

### 3. Use It

```kotlin
import io.blackarrows.http.io.HttpRequestExecutor
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@Serializable
data class User(val id: Int, val name: String, val email: String)

class UserRepository(
    private val httpExecutor: HttpRequestExecutor
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getUsers(): Result<List<User>> {
        return try {
            val response = httpExecutor.getJson(
                url = "https://api.example.com/users",
                authRequired = true
            )
            val bodyString = response.body?.decodeToString() ?: "[]"
            val users = json.decodeFromString(ListSerializer(User.serializer()), bodyString)
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createUser(user: User): Result<User> {
        return try {
            val response = httpExecutor.postJson(
                url = "https://api.example.com/users",
                body = user,
                authRequired = true
            )
            val bodyString = response.body?.decodeToString() ?: throw IllegalStateException("Empty response")
            val createdUser = json.decodeFromString(User.serializer(), bodyString)
            Result.success(createdUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### 4. Handle Errors

```kotlin
import io.blackarrows.http.errors.*

try {
    val users = repository.getUsers()
    // Success
} catch (e: AuthException) {
    when (e.errorCode) {
        ErrorCodes.AUTH_TOKEN_EXPIRED -> refreshTokenAndRetry()
        ErrorCodes.AUTH_INSUFFICIENT_PERMISSIONS -> showPermissionDenied()
    }
} catch (e: NetworkException) {
    if (e.isRetryable) {
        scheduleRetry()
    } else {
        showNetworkError()
    }
} catch (e: HttpStatusException) {
    when (e.statusCode) {
        404 -> showNotFound()
        429 -> handleRateLimiting()
        in 500..599 -> showServerError()
    }
}
```

## Modules

### [http-core](http-core/README.md)

Client-agnostic abstractions and interfaces. Contains:
- `HttpRequestExecutor` - Main interface for HTTP operations
- `HttpHeaders` & `HttpRequestConfig` - Framework-independent types
- Exception hierarchy - Type-safe error handling
- Interceptor system - Policy-based request/response handling
- `ApiResponse` - Response wrapper with JSON deserialization

**Zero implementation dependencies** - Write your business logic once, swap clients anytime.

[📖 Full Documentation](http-core/README.md)

### [http-ktor](http-ktor/README.md)

Ktor-based implementation with platform-specific optimizations:
- **Android/JVM**: OkHttp engine
- **iOS**: Darwin engine (native iOS networking)
- Automatic JSON serialization/deserialization
- Koin integration module
- Built-in logging and content negotiation

[📖 Full Documentation](http-ktor/README.md)

### Coming Soon

Additional HTTP client implementations will be added based on **community demand**:
- `http-okhttp` - Direct OkHttp implementation for Android
- `http-urlconnection` - Pure Java URLConnection for lightweight JVM apps
- `http-js` - JavaScript fetch API for Kotlin/JS targets

**Want a specific client?** [Open an issue](https://github.com/E5c11/arrow-http/issues) to request it!

## Supported Platforms

| Platform | Status | Engine (Ktor) |
|----------|--------|---------------|
| Android  | ✅     | OkHttp        |
| JVM      | ✅     | OkHttp        |
| iOS arm64 | ✅    | Darwin        |
| iOS x64  | ✅     | Darwin        |
| iOS simulatorArm64 | ✅ | Darwin   |

## Features

### 🔒 Type-Safe Error Handling

Comprehensive exception hierarchy for precise error handling:

```kotlin
sealed class HttpException
├── NetworkException        // Network failures (retryable)
├── AuthException          // Auth/permission errors
├── HttpStatusException    // HTTP status codes (4xx, 5xx)
├── TimeoutException       // Request timeouts
└── SerializationException // JSON parsing errors
```

Each exception includes:
- Error codes for specific scenarios
- Original stack traces
- Retry recommendations
- HTTP status codes (where applicable)

### 🔄 Built-in Interceptors

Policy-based interceptor system for cross-cutting concerns:

```kotlin
val executor = KtorHttpRequestExecutor(
    client = httpClient,
    authHeaderProvider = headerProvider,
    policies = listOf(
        AuthPolicy(authRefresher, maxRetries = 2),  // Automatic token refresh
        RetryPolicy(maxRetries = 3, exponentialBackoff = true)  // Retry transient failures
    )
)
```

**AuthPolicy**: Automatically refreshes expired tokens and retries requests
**RetryPolicy**: Handles transient network failures with exponential backoff

### 📝 Comprehensive HTTP Methods

All common HTTP operations with typed requests and responses:

```kotlin
// GET
executor.getJson(url, queryParams, headers, authRequired)
executor.getRaw(url, headers, queryParams, authRequired)

// POST
executor.postJson(url, body, headers, authRequired)
executor.postForm(url, formParams, headers, authRequired)
executor.postMultipart(url, multipartForm, headers, authRequired)
executor.postQuery(url, queryParams, contentType, headers, authRequired)

// PUT
executor.putJson(url, body, headers, authRequired)
executor.putRaw(url, body, contentType, headers, authRequired)

// DELETE
executor.deleteJson(url, body, headers, authRequired)
executor.deleteRaw(url, body, contentType, headers, authRequired)
```

### 📤 File Uploads

Simple multipart form data support:

```kotlin
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

executor.postMultipart("https://api.example.com/upload", form, authRequired = true)
```

### ⚙️ Flexible Configuration

Per-request configuration:

```kotlin
val config = HttpRequestConfig(
    headers = HttpHeaders.of("X-Request-ID" to "12345"),
    queryParams = mapOf("debug" to "true"),
    timeout = 60_000L,
    followRedirects = false
)

val response = executor.getJson(url, config = config)
```

## Architecture

```
┌─────────────────────────────────────┐
│      Your Application Code          │
│  (Platform: Android/iOS/JVM)        │
└─────────────────┬───────────────────┘
                  │
                  ▼
┌─────────────────────────────────────┐
│         http-core                   │
│  • HttpRequestExecutor              │
│  • HttpHeaders, HttpRequestConfig   │
│  • Exception Hierarchy              │
│  • Interceptor System               │
│  • ApiResponse                      │
└─────────────────┬───────────────────┘
                  │
                  ▼
┌─────────────────────────────────────┐
│       http-ktor (or other)          │
│  • KtorHttpRequestExecutor          │
│  • Platform-Specific Engines        │
│  • JSON Serialization               │
│  • Error Mapping                    │
└─────────────────┬───────────────────┘
                  │
                  ▼
┌─────────────────────────────────────┐
│    Platform HTTP Implementation     │
│  Android: OkHttp                    │
│  iOS: Darwin (NSURLSession)         │
│  JVM: OkHttp                        │
└─────────────────────────────────────┘
```

**Key Principle**: Your application code depends only on `http-core` abstractions. The implementation module (`http-ktor`) is a runtime dependency that can be swapped without changing your business logic.

## Manual Setup (Without Koin)

If you prefer not to use dependency injection:

```kotlin
import io.blackarrows.http.ktor.createHttpClient
import io.blackarrows.http.ktor.KtorHttpRequestExecutor
import io.blackarrows.http.providers.HeaderProvider
import io.blackarrows.http.io.interceptors.*

// 1. Create HTTP client
val httpClient = createHttpClient {
    install(HttpTimeout) {
        requestTimeoutMillis = 60_000
    }
}

// 2. Create header provider
val headerProvider = object : HeaderProvider {
    override suspend fun getHeaders(vararg additional: Pair<String, String>): Map<String, String> {
        return mapOf("Authorization" to "Bearer ${tokenStore.getToken()}")
    }
}

// 3. Create policies
val authPolicy = AuthPolicy(
    authRefresher = object : AuthRefresher {
        override suspend fun refresh(): ReauthResult {
            return try {
                tokenStore.refreshToken()
                ReauthResult.Success
            } catch (e: Exception) {
                ReauthResult.Failed
            }
        }
    }
)

val retryPolicy = RetryPolicy(maxRetries = 3)

// 4. Create executor
val httpExecutor = KtorHttpRequestExecutor(
    client = httpClient,
    authHeaderProvider = headerProvider,
    policies = listOf(authPolicy, retryPolicy)
)
```

## Testing

Mock the `HttpRequestExecutor` interface for easy testing:

```kotlin
class MockHttpExecutor : HttpRequestExecutor {
    var mockResponse: ApiResponse? = null

    override suspend fun getJson(
        url: String,
        queryParams: Map<String, String>,
        headers: HttpHeaders,
        authRequired: Boolean,
        config: HttpRequestConfig
    ): ApiResponse {
        return mockResponse ?: error("No mock response configured")
    }

    // Implement other methods...
}

@Test
fun testRepository() = runTest {
    val mockExecutor = MockHttpExecutor()
    mockExecutor.mockResponse = MockApiResponse(
        statusCode = 200,
        body = """[{"id":1,"name":"Test"}]"""
    )

    val repository = UserRepository(mockExecutor)
    val users = repository.getUsers()

    assertEquals(1, users.size)
    assertEquals("Test", users[0].name)
}
```

## Sample Application

Check out the [`sample`](sample/) module for a complete Android demo app that showcases:

- **GET JSON**: Fetching and displaying images from Picsum Photos API
- **POST JSON**: Creating posts via JSONPlaceholder API
- **GET Raw/Binary**: Downloading raw file bytes
- **PUT JSON**: Updating existing resources
- **DELETE**: Deleting resources by ID
- **POST Form**: Submitting form-urlencoded data

The sample app demonstrates:
- Proper Koin DI setup with the library
- Repository pattern implementation
- MVVM architecture with ViewModels
- Error handling and loading states
- Jetpack Compose UI

**To run the sample app:**
```bash
./gradlew :sample:assembleDebug
# or open in Android Studio and run the 'sample' configuration
```

## Contributing

Contributions are welcome! Please:
1. Open an issue to discuss the change
2. Fork the repository
3. Create a feature branch
4. Submit a pull request

### Requesting New HTTP Clients

Want support for a different HTTP client? Open an issue with:
- Client name (OkHttp, URLConnection, etc.)
- Use case / why you need it
- Target platforms

Client implementations will be prioritized based on community demand.

## Roadmap

- [x] Core abstractions (`http-core`)
- [x] Ktor implementation (`http-ktor`)
- [x] Maven Central publishing
- [x] Sample Android application
- [ ] Comprehensive test suite
- [ ] Additional client implementations (based on demand)
- [ ] WebSocket support
- [ ] GraphQL utilities
- [ ] Response caching

## Requirements

- Kotlin 2.1.0+
- Gradle 8.11+
- Android minSdk 24+ (for Android targets)
- iOS 13+ (for iOS targets)
- JVM 17+ (for JVM targets)

## License

```
Copyright 2025 Emmanuel Conradie

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## Author

**Emmanuel Conradie**
GitHub: [@E5c11](https://github.com/E5c11)

## Support

- 📖 [Documentation](https://github.com/E5c11/arrow-http/wiki) (coming soon)
- 🐛 [Report Issues](https://github.com/E5c11/arrow-http/issues)
- 💬 [Discussions](https://github.com/E5c11/arrow-http/discussions)
- ⭐ Star the repo if you find it useful!

---

Built with ❤️ for the Kotlin Multiplatform community