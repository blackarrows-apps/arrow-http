package io.blackarrows.http.providers

interface HeaderProvider {
    suspend fun getHeaders(vararg additional: Pair<String, String>): Map<String, String>

    fun invalidate()
}
