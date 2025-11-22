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
    }
}

android {
    namespace = "io.blackarrows.http"
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
version = "1.0.0"

mavenPublishing {
    coordinates("io.github.blackarrows-apps", "http-core", "1.0.0")

    pom {
        name.set("Arrow HTTP Core")
        description.set("Client-agnostic HTTP abstractions for Kotlin Multiplatform")
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
