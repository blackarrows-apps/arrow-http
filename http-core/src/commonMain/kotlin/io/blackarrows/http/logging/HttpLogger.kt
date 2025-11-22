package io.blackarrows.http.logging

interface HttpLogger {
    fun message(message: String)

    fun exception(
        message: String,
        throwable: Throwable? = null,
    )
}
