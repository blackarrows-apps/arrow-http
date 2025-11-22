package io.blackarrows.http.errors

/**
 * Standard HTTP status codes and custom application error codes.
 *
 * This object provides constants for:
 * - Standard HTTP status codes (1xx, 2xx, 3xx, 4xx, 5xx)
 * - Custom application-specific error codes
 */
object ErrorCodes {

    // -------------------------------------------------------------------------
    // HTTP Status Codes - Informational (1xx)
    // -------------------------------------------------------------------------

    /** 100 Continue - The server has received the request headers. */
    const val HTTP_CONTINUE = 100

    /** 101 Switching Protocols - The server is switching protocols. */
    const val HTTP_SWITCHING_PROTOCOLS = 101

    /** 102 Processing - The server is processing the request. */
    const val HTTP_PROCESSING = 102

    // -------------------------------------------------------------------------
    // HTTP Status Codes - Success (2xx)
    // -------------------------------------------------------------------------

    /** 200 OK - The request succeeded. */
    const val HTTP_OK = 200

    /** 201 Created - The request succeeded and a new resource was created. */
    const val HTTP_CREATED = 201

    /** 202 Accepted - The request has been accepted for processing. */
    const val HTTP_ACCEPTED = 202

    /** 204 No Content - The request succeeded but returns no content. */
    const val HTTP_NO_CONTENT = 204

    /** 205 Reset Content - The request succeeded and the client should reset the document view. */
    const val HTTP_RESET_CONTENT = 205

    /** 206 Partial Content - The server is delivering only part of the resource. */
    const val HTTP_PARTIAL_CONTENT = 206

    // -------------------------------------------------------------------------
    // HTTP Status Codes - Redirection (3xx)
    // -------------------------------------------------------------------------

    /** 300 Multiple Choices - Multiple options for the resource. */
    const val HTTP_MULTIPLE_CHOICES = 300

    /** 301 Moved Permanently - The resource has been moved permanently. */
    const val HTTP_MOVED_PERMANENTLY = 301

    /** 302 Found - The resource has been found at a different URI. */
    const val HTTP_FOUND = 302

    /** 303 See Other - The response can be found under a different URI. */
    const val HTTP_SEE_OTHER = 303

    /** 304 Not Modified - The resource has not been modified. */
    const val HTTP_NOT_MODIFIED = 304

    /** 307 Temporary Redirect - The resource is temporarily at a different URI. */
    const val HTTP_TEMPORARY_REDIRECT = 307

    /** 308 Permanent Redirect - The resource is permanently at a different URI. */
    const val HTTP_PERMANENT_REDIRECT = 308

    // -------------------------------------------------------------------------
    // HTTP Status Codes - Client Errors (4xx)
    // -------------------------------------------------------------------------

    /** 400 Bad Request - The server cannot process the request due to client error. */
    const val HTTP_BAD_REQUEST = 400

    /** 401 Unauthorized - Authentication is required and has failed or not been provided. */
    const val HTTP_UNAUTHORIZED = 401

    /** 402 Payment Required - Reserved for future use. */
    const val HTTP_PAYMENT_REQUIRED = 402

    /** 403 Forbidden - The server understood the request but refuses to authorize it. */
    const val HTTP_FORBIDDEN = 403

    /** 404 Not Found - The requested resource could not be found. */
    const val HTTP_NOT_FOUND = 404

    /** 405 Method Not Allowed - The request method is not supported for the resource. */
    const val HTTP_METHOD_NOT_ALLOWED = 405

    /** 406 Not Acceptable - The resource is not available in a format acceptable to the client. */
    const val HTTP_NOT_ACCEPTABLE = 406

    /** 407 Proxy Authentication Required - Authentication with a proxy is required. */
    const val HTTP_PROXY_AUTHENTICATION_REQUIRED = 407

    /** 408 Request Timeout - The server timed out waiting for the request. */
    const val HTTP_REQUEST_TIMEOUT = 408

    /** 409 Conflict - The request conflicts with the current state of the server. */
    const val HTTP_CONFLICT = 409

    /** 410 Gone - The resource is no longer available and will not be available again. */
    const val HTTP_GONE = 410

    /** 411 Length Required - The request did not specify the length of its content. */
    const val HTTP_LENGTH_REQUIRED = 411

    /** 412 Precondition Failed - The server does not meet one of the preconditions. */
    const val HTTP_PRECONDITION_FAILED = 412

    /** 413 Payload Too Large - The request entity is larger than the server is willing to process. */
    const val HTTP_PAYLOAD_TOO_LARGE = 413

    /** 414 URI Too Long - The URI provided was too long for the server to process. */
    const val HTTP_URI_TOO_LONG = 414

    /** 415 Unsupported Media Type - The media type of the request is not supported. */
    const val HTTP_UNSUPPORTED_MEDIA_TYPE = 415

    /** 416 Range Not Satisfiable - The range specified in the request cannot be fulfilled. */
    const val HTTP_RANGE_NOT_SATISFIABLE = 416

    /** 417 Expectation Failed - The server cannot meet the requirements of the Expect header. */
    const val HTTP_EXPECTATION_FAILED = 417

    /** 418 I'm a teapot - The server refuses to brew coffee because it is a teapot. */
    const val HTTP_IM_A_TEAPOT = 418

    /** 421 Misdirected Request - The request was directed at a server that cannot produce a response. */
    const val HTTP_MISDIRECTED_REQUEST = 421

    /** 422 Unprocessable Entity - The request was well-formed but contains semantic errors. */
    const val HTTP_UNPROCESSABLE_ENTITY = 422

    /** 423 Locked - The resource being accessed is locked. */
    const val HTTP_LOCKED = 423

    /** 424 Failed Dependency - The request failed due to failure of a previous request. */
    const val HTTP_FAILED_DEPENDENCY = 424

    /** 425 Too Early - The server is unwilling to process a request that might be replayed. */
    const val HTTP_TOO_EARLY = 425

    /** 426 Upgrade Required - The client should switch to a different protocol. */
    const val HTTP_UPGRADE_REQUIRED = 426

    /** 428 Precondition Required - The origin server requires the request to be conditional. */
    const val HTTP_PRECONDITION_REQUIRED = 428

    /** 429 Too Many Requests - The user has sent too many requests in a given time. */
    const val HTTP_TOO_MANY_REQUESTS = 429

    /** 431 Request Header Fields Too Large - The server is unwilling to process the request. */
    const val HTTP_REQUEST_HEADER_FIELDS_TOO_LARGE = 431

    /** 451 Unavailable For Legal Reasons - The resource is unavailable for legal reasons. */
    const val HTTP_UNAVAILABLE_FOR_LEGAL_REASONS = 451

    // -------------------------------------------------------------------------
    // HTTP Status Codes - Server Errors (5xx)
    // -------------------------------------------------------------------------

    /** 500 Internal Server Error - The server encountered an unexpected condition. */
    const val HTTP_INTERNAL_SERVER_ERROR = 500

    /** 501 Not Implemented - The server does not support the functionality required. */
    const val HTTP_NOT_IMPLEMENTED = 501

    /** 502 Bad Gateway - The server received an invalid response from an upstream server. */
    const val HTTP_BAD_GATEWAY = 502

    /** 503 Service Unavailable - The server is currently unavailable. */
    const val HTTP_SERVICE_UNAVAILABLE = 503

    /** 504 Gateway Timeout - The server did not receive a timely response from an upstream server. */
    const val HTTP_GATEWAY_TIMEOUT = 504

    /** 505 HTTP Version Not Supported - The server does not support the HTTP version. */
    const val HTTP_VERSION_NOT_SUPPORTED = 505

    /** 506 Variant Also Negotiates - The server has an internal configuration error. */
    const val HTTP_VARIANT_ALSO_NEGOTIATES = 506

    /** 507 Insufficient Storage - The server is unable to store the representation. */
    const val HTTP_INSUFFICIENT_STORAGE = 507

    /** 508 Loop Detected - The server detected an infinite loop while processing the request. */
    const val HTTP_LOOP_DETECTED = 508

    /** 510 Not Extended - Further extensions to the request are required. */
    const val HTTP_NOT_EXTENDED = 510

    /** 511 Network Authentication Required - The client needs to authenticate to gain network access. */
    const val HTTP_NETWORK_AUTHENTICATION_REQUIRED = 511

    // -------------------------------------------------------------------------
    // Custom Application Error Codes
    // -------------------------------------------------------------------------

    // Authentication & Authorization (AUTH_xxx)
    /** Custom error code: Token has expired. */
    const val AUTH_TOKEN_EXPIRED = "AUTH_001"

    /** Custom error code: Token is invalid or malformed. */
    const val AUTH_TOKEN_INVALID = "AUTH_002"

    /** Custom error code: Token refresh failed. */
    const val AUTH_REFRESH_FAILED = "AUTH_003"

    /** Custom error code: Insufficient permissions. */
    const val AUTH_INSUFFICIENT_PERMISSIONS = "AUTH_004"

    /** Custom error code: Account is locked or disabled. */
    const val AUTH_ACCOUNT_LOCKED = "AUTH_005"

    /** Custom error code: Maximum reauthentication attempts exceeded. */
    const val AUTH_MAX_RETRIES_EXCEEDED = "AUTH_006"

    // Network Errors (NET_xxx)
    /** Custom error code: Network connection unavailable. */
    const val NET_CONNECTION_UNAVAILABLE = "NET_001"

    /** Custom error code: Connection timeout. */
    const val NET_CONNECTION_TIMEOUT = "NET_002"

    /** Custom error code: Request timeout. */
    const val NET_REQUEST_TIMEOUT = "NET_003"

    /** Custom error code: DNS resolution failed. */
    const val NET_DNS_FAILURE = "NET_004"

    /** Custom error code: SSL/TLS handshake failed. */
    const val NET_SSL_HANDSHAKE_FAILED = "NET_005"

    /** Custom error code: Maximum retry attempts exceeded. */
    const val NET_MAX_RETRIES_EXCEEDED = "NET_006"

    // Serialization Errors (SER_xxx)
    /** Custom error code: JSON serialization failed. */
    const val SER_SERIALIZATION_FAILED = "SER_001"

    /** Custom error code: JSON deserialization failed. */
    const val SER_DESERIALIZATION_FAILED = "SER_002"

    /** Custom error code: Invalid JSON format. */
    const val SER_INVALID_JSON = "SER_003"

    /** Custom error code: Type mismatch during deserialization. */
    const val SER_TYPE_MISMATCH = "SER_004"

    // Validation Errors (VAL_xxx)
    /** Custom error code: Required field is missing. */
    const val VAL_REQUIRED_FIELD_MISSING = "VAL_001"

    /** Custom error code: Invalid field format. */
    const val VAL_INVALID_FORMAT = "VAL_002"

    /** Custom error code: Value out of range. */
    const val VAL_OUT_OF_RANGE = "VAL_003"

    /** Custom error code: Invalid email format. */
    const val VAL_INVALID_EMAIL = "VAL_004"

    // Rate Limiting (RATE_xxx)
    /** Custom error code: Rate limit exceeded. */
    const val RATE_LIMIT_EXCEEDED = "RATE_001"

    /** Custom error code: Quota exceeded. */
    const val RATE_QUOTA_EXCEEDED = "RATE_002"

    // File Upload/Download (FILE_xxx)
    /** Custom error code: File is too large. */
    const val FILE_TOO_LARGE = "FILE_001"

    /** Custom error code: Unsupported file type. */
    const val FILE_UNSUPPORTED_TYPE = "FILE_002"

    /** Custom error code: File upload failed. */
    const val FILE_UPLOAD_FAILED = "FILE_003"

    /** Custom error code: File download failed. */
    const val FILE_DOWNLOAD_FAILED = "FILE_004"

    // -------------------------------------------------------------------------
    // Helper Functions
    // -------------------------------------------------------------------------

    /**
     * Checks if the given HTTP status code represents a successful response (2xx).
     */
    fun isSuccessful(statusCode: Int): Boolean = statusCode in 200..299

    /**
     * Checks if the given HTTP status code represents a client error (4xx).
     */
    fun isClientError(statusCode: Int): Boolean = statusCode in 400..499

    /**
     * Checks if the given HTTP status code represents a server error (5xx).
     */
    fun isServerError(statusCode: Int): Boolean = statusCode in 500..599

    /**
     * Checks if the given HTTP status code represents a redirection (3xx).
     */
    fun isRedirection(statusCode: Int): Boolean = statusCode in 300..399

    /**
     * Checks if the given HTTP status code represents an error (4xx or 5xx).
     */
    fun isError(statusCode: Int): Boolean = isClientError(statusCode) || isServerError(statusCode)

    /**
     * Gets a human-readable description for a standard HTTP status code.
     */
    fun getHttpStatusDescription(statusCode: Int): String = when (statusCode) {
        HTTP_OK -> "OK"
        HTTP_CREATED -> "Created"
        HTTP_ACCEPTED -> "Accepted"
        HTTP_NO_CONTENT -> "No Content"
        HTTP_BAD_REQUEST -> "Bad Request"
        HTTP_UNAUTHORIZED -> "Unauthorized"
        HTTP_FORBIDDEN -> "Forbidden"
        HTTP_NOT_FOUND -> "Not Found"
        HTTP_METHOD_NOT_ALLOWED -> "Method Not Allowed"
        HTTP_CONFLICT -> "Conflict"
        HTTP_TOO_MANY_REQUESTS -> "Too Many Requests"
        HTTP_INTERNAL_SERVER_ERROR -> "Internal Server Error"
        HTTP_BAD_GATEWAY -> "Bad Gateway"
        HTTP_SERVICE_UNAVAILABLE -> "Service Unavailable"
        HTTP_GATEWAY_TIMEOUT -> "Gateway Timeout"
        else -> "HTTP $statusCode"
    }
}
