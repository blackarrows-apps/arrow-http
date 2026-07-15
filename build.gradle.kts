import org.gradle.plugins.signing.Sign

plugins {
    base
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

// A local verification repository is intentionally unsigned: its only consumer is the
// isolated fixture in this checkout. Central publications still require every signature.
val isLocalPublicationVerification = gradle.startParameter.taskNames.any {
    it.contains("VerificationRepository", ignoreCase = true)
}

subprojects {
    tasks.withType<Sign>().configureEach {
        enabled = !isLocalPublicationVerification
    }
}

// NOTE: do NOT tasks.register("clean", Delete::class) here — with both a `js` and a
// `wasmJs` target declared across subprojects, Kotlin's Node.js root plugin applies the
// Gradle `base` plugin itself, which tries to create its own `clean` task and collides
// with a manually-registered one (KT-69996: "Cannot add task 'clean' as a task with that
// name already exists"). Applying `base` explicitly above and configuring its `clean`
// task instead avoids the duplicate-registration race.
tasks.clean {
    delete(rootProject.layout.buildDirectory)
}

tasks.register("publishAllPublicationsToVerificationRepository") {
    group = "publishing"
    description = "Publishes all library modules to the local external-consumer repository."
    dependsOn(
        ":http-core:publishAllPublicationsToVerificationRepository",
        ":http-ktor:publishAllPublicationsToVerificationRepository",
        ":http-test:publishAllPublicationsToVerificationRepository",
    )
}

// Stub task required when arrow-http is included as a composite build inside a
// Kotlin/wasmJs host project. The Kotlin wasmJs plugin on the host walks all
// included builds looking for a 'rootPackageJson' task to aggregate npm
// dependencies. Deferred to after subproject evaluation and only registered if
// missing: arrow-http's own `js`/`wasmJs` targets (http-core, http-ktor) already
// cause Kotlin's real NodeJsRootPlugin to create a real 'rootPackageJson' task on
// this project when built standalone, and registering ours unconditionally would
// collide with it (duplicate task name).
gradle.projectsEvaluated {
    if (tasks.findByName("rootPackageJson") == null) {
        tasks.register("rootPackageJson") {
            description = "Composite-build stub: no npm dependencies in arrow-http."
        }
    }
}
