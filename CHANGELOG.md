# Changelog

All notable changes to this project will be documented in this file.

## [1.3.2]

### Fixed
- Disable HTTP response caching on the iOS Darwin engine's requests
  (`NSURLRequestReloadIgnoringLocalCacheData`). Unlike the Android/OkHttp engine, which never
  caches responses to disk by default, Darwin's default config routes every request through
  `NSURLSession`'s standard cache policy against the shared, disk-backed `NSURLCache` --
  honoring the response's `Cache-Control`/`ETag`/`Last-Modified` (or even RFC 7234 heuristic
  freshness with no cache headers at all) and persisting across app relaunches. Consumers
  hitting endpoints they expect to be freshly fetched every call could silently get a stale
  cached response on iOS with no equivalent behavior on Android.

## [1.3.1]

### Added
- `createHttpClient` now accepts optional `requestTimeoutMillis`, `connectTimeoutMillis`,
  and `socketTimeoutMillis` overrides so consuming apps can tune timeouts for their own
  endpoints instead of being locked to the library's per-platform defaults.

### Fixed
- Install Ktor's `HttpTimeout` plugin on Android, iOS, and JVM. Without it, the OkHttp
  engine fell back to OkHttp's bare 10s read timeout on every request, which is too short
  for endpoints that do real synchronous work server-side (e.g. a Cloud Run cold start).

## [1.3.0]

### Fixed
- Construct the Ktor client with the Darwin engine on iOS instead of throwing
  `NotImplementedError` at runtime.
- Construct the Ktor client with the OkHttp engine on JVM instead of throwing
  `NotImplementedError` at runtime.

### Verification
- Add a clean external Maven publication consumer for `iosArm64` and
  `iosSimulatorArm64`, including a no-network Darwin client construction test.
- Stage the complete publication set in a dedicated local Maven repository before release,
  preventing root metadata from advertising Apple target modules that were not uploaded.

## [1.2.0]

### Added
- PATCH verb support (`patchJson`/`patchRaw`) on `HttpRequestExecutor` and
  `KtorHttpRequestExecutor`.
- `js` (Kotlin/JS) target for `http-core` and `http-ktor`, published in library mode
  with generated TypeScript definitions.
