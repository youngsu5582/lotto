package toss

import org.springframework.http.HttpHeaders
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestClient
import purchase.domain.PaymentClient
import purchase.domain.PurchaseException
import purchase.domain.PurchaseExceptionCode
import purchase.domain.vo.CancelData
import purchase.domain.vo.CancelRequest
import purchase.domain.vo.PurchaseData
import purchase.domain.vo.PurchaseRequest
import toss.config.TossClientProperties
import toss.dto.TossPaymentCancelRequest
import toss.dto.TossPaymentCancelResponse
import toss.dto.TossPaymentConfirmRequest
import toss.dto.TossPaymentConfirmResponse
import java.util.*

class TossPaymentClient(
    private val restClient: RestClient,
    private val tossClientProperties: TossClientProperties,
    private val tossResponseErrorHandler: TossResponseErrorHandler
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
        val response = executeWithRetry {
            sendRequest(tossClientProperties.paymentUrl, confirmRequest, TossPaymentConfirmResponse::class.java)
        }
        return response.toPurchaseData()
    }

    override fun cancel(request: CancelRequest): CancelData {
        val cancelRequest = TossPaymentCancelRequest(
            cancelReason = request.cancelReason,
        )
        val response = executeWithRetry {
            sendRequest(
                tossClientProperties.cancelUrl,
                cancelRequest,
                TossPaymentCancelResponse::class.java,
                request.paymentKey
            )
        }
        return response.toCancelData()
    }

    override fun support(provider: String): Boolean {
        return provider == "TOSS"
    }

    private fun <T> executeWithRetry(block: () -> T): T {
        return try {
            retry(maxRetries) {
                block()
            }
        } catch (e: PurchaseException) {
            throw e
        } catch (e: Exception) {
            throw PurchaseException(PurchaseExceptionCode.FAILED, e)
        }
    }


    private fun <T> sendRequest(url: String, request: Any, responseClass: Class<T>, vararg variables: Any): T {
        return restClient.post()
            .uri(url, *variables)
            .header(HttpHeaders.AUTHORIZATION, "Basic $authorizationKey")
            .body(request)
            .retrieve()
            .onStatus(tossResponseErrorHandler)
            .body(responseClass)!!
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
