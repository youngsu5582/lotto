package toss

import com.fasterxml.jackson.databind.ObjectMapper
import common.business.Implementation
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResponseErrorHandler
import purchase.domain.PurchaseException
import purchase.domain.PurchaseExceptionCode
import toss.dto.TossPaymentErrorResponse

@Implementation
class TossResponseErrorHandler(
    private val objectMapper: ObjectMapper = ObjectMapper()
) : ResponseErrorHandler {
    override fun hasError(response: ClientHttpResponse): Boolean {
        return response.statusCode.isError
    }

    override fun handleError(response: ClientHttpResponse) {
        return handleErrorResponse(response)
    }

    private fun handleErrorResponse(response: ClientHttpResponse) {
        val errorCode = extractErrorCode(response)
        throw when {
            errorCode.isRetryable() -> RetryableException(errorCode.message)
            else -> PurchaseException(PurchaseExceptionCode.FAILED, errorCode.message)
        }
    }

    private fun extractErrorCode(response: ClientHttpResponse): TossPaymentErrorCode {
        val errorResponse = objectMapper.readValue(response.body, TossPaymentErrorResponse::class.java)
        return TossPaymentErrorCode.fromCode(errorResponse.code)
    }
}
