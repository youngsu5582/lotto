package common.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val success: Boolean = true,
    val status: Int = 200,
    val message: String? = "ok",
    val data: T? = null,
    @JsonIgnore
    val headers: Map<String, String> = emptyMap(),
) {
    companion object {
        fun <T> fail(data: T? = null, message: String? = ""): ApiResponse<T> {
            return ApiResponse(
                false, 400, message, data
            )
        }

        fun <T> ok(data: T? = null, message: String? = ""): ApiResponse<T> {
            return ApiResponse(
                true, 200, message, data
            )
        }
    }
}
