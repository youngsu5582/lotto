package common.dto

/**
 * ApiResponse를 DSL 형식으로 만들기 위한 함수
 *
 * 사용 예:
 * val response = apiResponse<String> {
 *     success = false
 *     status = 404
 *     message = "Not Found"
 *     data = null
 *     header("X-Error-Code", "1234")
 *     headers {
 *         put("X-Another-Header", "value")
 *     }
 * }
 */
fun <T> apiResponse(block: ApiResponseDsl<T>.() -> Unit): ApiResponse<T> =
    ApiResponseDsl<T>().apply(block).build()

class ApiResponseDsl<T> {
    var success: Boolean = true
    var status: Int = 200
    var message: String? = "ok"
    var data: T? = null

    private val _headers: MutableMap<String, String> = mutableMapOf()

    /**
     * 단일 헤더 추가용 함수
     */
    fun header(key: String, value: String) {
        _headers[key] = value
    }

    /**
     * 복수 헤더 설정용 함수
     */
    fun headers(block: MutableMap<String, String>.() -> Unit) {
        _headers.apply(block)
    }

    fun build(): ApiResponse<T> =
        ApiResponse(
            success = success,
            status = status,
            message = message,
            data = data,
            headers = _headers.toMap()
        )
}
