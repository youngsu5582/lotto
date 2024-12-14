package common.dto

import com.fasterxml.jackson.annotation.JsonIgnore

data class ApiResponse<T>(
    val status: Int = 200,
    val message: String = "ok",
    val data: T? = null,
    @JsonIgnore
    val headers: Map<String, String> = emptyMap()
)
