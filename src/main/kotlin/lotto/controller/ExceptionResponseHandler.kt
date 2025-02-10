package lotto.controller

import common.dto.ApiResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import purchase.domain.PurchaseException

private val logger = KotlinLogging.logger {}

@ControllerAdvice
class LottoExceptionHandler {
    @ExceptionHandler(PurchaseException::class)
    fun handlePurchaseException(ex: PurchaseException): ApiResponse<Void> {
        logger.warn { ex.stackTraceToString() }
        return ApiResponse.fail(message = ex.message)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalException(ex: IllegalArgumentException): ApiResponse<Void> {
        logger.warn { ex.stackTraceToString() }
        return ApiResponse.fail(message = ex.message)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(ex: NoSuchElementException): ApiResponse<Void> {
        logger.warn { ex.stackTraceToString() }
        return ApiResponse.fail(message = ex.message)
    }
}
