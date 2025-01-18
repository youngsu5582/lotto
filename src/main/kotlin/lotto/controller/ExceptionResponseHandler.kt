package lotto.controller

import common.dto.ApiResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import purchase.domain.PurchaseException

@ControllerAdvice
class ExceptionResponseHandler {
    @ExceptionHandler(PurchaseException::class)
    fun handlePurchaseException(ex: PurchaseException): ApiResponse<Void> {
        return ApiResponse.fail(message = ex.message)
    }
}
