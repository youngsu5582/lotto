package auth.config

import common.dto.ApiResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class AuthExceptionHandler {

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(ex: ResponseStatusException): ApiResponse<Any?> {
        return ApiResponse(
            success = false,
            status = ex.statusCode.value(),
            message = ex.reason ?: "에러가 발생했습니다.",
            data = null
        )
    }
}
