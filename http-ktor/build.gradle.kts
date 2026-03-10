plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.maven.central.publish)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    jvm()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                // Depend on http-core
                api(project(":http-core"))

                // Ktor
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.logging)

                // Koin
                implementation(libs.koin.core)

                // Kotlinx
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
    }
}

android {
    namespace = "io.blackarrows.http.ktor"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

group = "io.github.blackarrows-apps"
version = "1.0.1"

mavenPublishing {
    coordinates("io.github.blackarrows-apps", "http-ktor", "1.0.1")

    pom {
        name.set("Arrow HTTP Ktor")
        description.set("Ktor-based HTTP client implementation for Kotlin Multiplatform")
        url.set("https://github.com/blackarrows-apps/arrow-http")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("E5c11")
                name.set("Emmanuel Conradie")
                url.set("https://github.com/E5c11")
            }
        }

        scm {
            connection.set("scm:git:git://github.com/blackarrows-apps/arrow-http.git")
            developerConnection.set("scm:git:ssh://github.com/blackarrows-apps/arrow-http.git")
            url.set("https://github.com/blackarrows-apps/arrow-http")
        }
    }

    publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}
