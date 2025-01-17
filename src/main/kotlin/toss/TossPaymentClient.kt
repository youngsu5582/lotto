package toss

import com.fasterxml.jackson.databind.ObjectMapper
import common.business.Implementation
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestClient
import purchase.domain.PaymentClient
import purchase.domain.PurchaseException
import purchase.domain.PurchaseExceptionCode
import purchase.domain.vo.PurchaseData
import purchase.domain.vo.PurchaseRequest
import toss.config.TossClientProperties
import toss.dto.TossPaymentConfirmErrorResponse
import toss.dto.TossPaymentConfirmRequest
import toss.dto.TossPaymentResponse
import java.util.*

@Implementation
class TossPaymentClient(
    private val restClient: RestClient,
    private val tossClientProperties: TossClientProperties,
    private val objectMapper: ObjectMapper = ObjectMapper()
) : PaymentClient {

    private val maxRetries = 3
    private val backoffMillis = 200L
    private val authorizationKey: String = Base64.getEncoder()
        .encodeToString("${tossClientProperties.apiKey}:".toByteArray())

    override fun process(request: PurchaseRequest): PurchaseData {
        val confirmRequest = TossPaymentConfirmRequest(
            paymentKey = request.paymentKey,
            orderId = request.orderId,
            amount = request.amount.toLong()
        )
        return try {
            val response = retry(maxRetries) {
                sendRequest(tossClientProperties.paymentUrl, confirmRequest, TossPaymentResponse::class.java)
            }
            response.toPurchaseData()
        } catch (e: PurchaseException) {
            throw e
        } catch (e: Exception) {
            throw PurchaseException(PurchaseExceptionCode.FAILED, e)
        }
    }

    private fun <T> sendRequest(url: String, request: Any, responseClass: Class<T>): T {
        return restClient.post()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Basic $authorizationKey")
            .body(request)
            .retrieve()
            .onStatus({ it.isError }, ::handleErrorResponse)
            .body(responseClass)!!
    }

    private fun handleErrorResponse(request: HttpRequest, response: ClientHttpResponse) {
        val errorCode = extractErrorCode(response)
        throw when {
            errorCode.isRetryable() -> RetryableException(errorCode.message)
            else -> PurchaseException(PurchaseExceptionCode.FAILED, errorCode.message)
        }
    }

    private fun extractErrorCode(response: ClientHttpResponse): TossPaymentErrorCode {
        val errorResponse = objectMapper.readValue(response.body, TossPaymentConfirmErrorResponse::class.java)
        return TossPaymentErrorCode.fromCode(errorResponse.code)
    }

    private fun <T> retry(maxAttempts: Int, block: () -> T): T {
        repeat(maxAttempts - 1) { attempt ->
            try {
                return block()
            } catch (e: RetryableException) {
                handleRetryableException(attempt)
            } catch (e: ResourceAccessException) {
                handleResourceAccessException(e, attempt)
            }
        }
        return executeFinalAttempt(block)
    }

    private fun handleRetryableException(attempt: Int) {
        Thread.sleep(backoffMillis * (attempt + 1))
    }

    private fun handleResourceAccessException(e: ResourceAccessException, attempt: Int) {
        if (isConnectTimeoutError(e.message)) {
            Thread.sleep(backoffMillis * (attempt + 1))
        } else {
            throw PurchaseException(PurchaseExceptionCode.READ_TIMEOUT, e)
        }
    }

    private fun <T> executeFinalAttempt(block: () -> T): T {
        return try {
            block()
        } catch (e: RetryableException) {
            throw PurchaseException(PurchaseExceptionCode.RETRY_FAILED, e)
        } catch (e: ResourceAccessException) {
            throw determineResourceAccessException(e)
        }
    }

    private fun determineResourceAccessException(e: ResourceAccessException): PurchaseException {
        return if (isConnectTimeoutError(e.message)) {
            PurchaseException(PurchaseExceptionCode.CONNECT_TIMEOUT, e)
        } else {
            PurchaseException(PurchaseExceptionCode.READ_TIMEOUT, e)
        }
    }

    private fun isConnectTimeoutError(message: String?): Boolean {
        return message?.contains("Connect timed out", ignoreCase = true) == true
    }
}

class RetryableException(message: String) : RuntimeException(message)
