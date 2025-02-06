package docs

import io.restassured.http.Method

enum class HttpMethod {
    GET,
    PUT,
    POST,
    DELETE,
    HEAD,
    TRACE,
    OPTIONS,
    PATCH
}

fun HttpMethod.toMethod(): Method =
    Method.valueOf(this.name.uppercase())
