package io.blackarrows.http.io

/** Decodes the response body as a UTF-8 string, or null if the body is absent. */
fun ApiResponse.bodyAsString(): String? = body?.decodeToString()
