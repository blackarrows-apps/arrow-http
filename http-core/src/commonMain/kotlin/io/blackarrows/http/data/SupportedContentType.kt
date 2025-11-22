package io.blackarrows.http.data

enum class SupportedContentType(
    val value: String,
) {
    JSON("application/json"),
    TEXT("text/plain"),
    XML("application/xml"),
    HTML("text/html"),
    FORM("application/x-www-form-urlencoded"),
    OCTET_STREAM("application/octet-stream"),

    ZIP("application/zip"),
    GZIP("application/gzip"),
    SEVEN_ZIP("application/x-7z-compressed"),
    BROTLI("application/x-brotli"), // Non-standard but used by some servers
    TAR("application/x-tar"),
    ;

    companion object {
        fun from(contentType: String?): SupportedContentType? =
            if (contentType != null) {
                entries.firstOrNull {
                    contentType.contains(it.value, ignoreCase = true)
                }
            } else {
                null
            }
    }
}
