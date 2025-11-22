package io.blackarrows.http

data class HttpHeaders(val values: Map<String, String>) {
    operator fun plus(other: HttpHeaders) = HttpHeaders(values + other.values)

    operator fun plus(pair: Pair<String, String>) = HttpHeaders(values + pair)

    fun toMap(): Map<String, String> = values

    companion object {
        fun of(vararg pairs: Pair<String, String>) = HttpHeaders(mapOf(*pairs))

        val Empty = HttpHeaders(emptyMap())
    }
}
