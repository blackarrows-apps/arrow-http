plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}

// Stub task required when arrow-http is included as a composite build inside a
// Kotlin/wasmJs host project. The Kotlin wasmJs plugin on the host walks all
// included builds looking for a 'rootPackageJson' task to aggregate npm
// dependencies. Arrow-http has no npm dependencies, so this is a no-op.
tasks.register("rootPackageJson") {
    description = "Composite-build stub: no npm dependencies in arrow-http."
}
