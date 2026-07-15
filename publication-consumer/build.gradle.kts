plugins {
    kotlin("multiplatform") version "2.1.0"
}

val arrowHttpVersion = providers.gradleProperty("arrowHttpVersion").orElse("1.3.0")

kotlin {
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation("io.github.blackarrows-apps:http-core:${arrowHttpVersion.get()}")
            implementation("io.github.blackarrows-apps:http-ktor:${arrowHttpVersion.get()}")
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
