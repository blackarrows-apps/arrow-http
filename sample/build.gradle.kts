plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "io.blackarrows.http.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "io.blackarrows.http.app"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Published libraries from Maven Central
    implementation("io.github.blackarrows-apps:http-core:1.0.0")
    implementation("io.github.blackarrows-apps:http-ktor:1.0.0")

    // Koin for dependency injection
    implementation(libs.koin.core)
    implementation("io.insert-koin:koin-android:4.0.0")
    implementation("io.insert-koin:koin-androidx-compose:4.0.0")

    // Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)

    // Coil for image loading in Compose
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Android
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)

    // Debug
    debugImplementation(libs.compose.ui.tooling)
}