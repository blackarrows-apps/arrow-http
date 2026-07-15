# Changelog

All notable changes to this project will be documented in this file.

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
