package io.blackarrows.http

import kotlinx.serialization.json.Json

val LenientJson: Json =
    Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }
