package toss

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import purchase.domain.PaymentClient
import purchase.domain.PurchaseException
import purchase.domain.PurchaseExceptionCode
import purchase.domain.vo.*
import java.time.LocalDateTime
import kotlin.random.Random

/**
 * CREATE TABLE purchase_key (
 *     payment_key VARCHAR(50) NOT NULL,
 *     PRIMARY KEY (payment_key)
 * );
 */

class TossPaymentFakeClient(
) : PaymentClient {
    override fun process(request: PurchaseRequest): PurchaseData {
        ifCustomHeaderThrowException()
        Thread.sleep(Random.nextLong(100, 500))
        return PurchaseData(
            totalAmount = request.amount,
            paymentKey = request.paymentKey,
            orderId = request.orderId,
            status = PurchaseStatus.SUCCESS,
            purchaseProvider = PurchaseProvider.TOSS,
            method = PaymentMethod.CARD,
        )
    }

    override fun cancel(request: CancelRequest): CancelData {
        ifCustomHeaderThrowException()
        simulateDelay()
        return CancelData(
            paymentKey = request.paymentKey,
            orderId = request.orderId,
            cancels = listOf(
                Cancel(
                    "transactionKey",
                    cancelReason = request.cancelReason,
                    cancelAt = LocalDateTime.now(),
                    cancelAmount = request.amount.toLong()
                )
            ),
            status = CancelStatus.CANCELED,
            purchaseProvider = PurchaseProvider.TOSS
        )
    }

    override fun support(provider: String): Boolean {
        return true
    }

    private fun ifCustomHeaderThrowException() {
        getCustomHeader()?.let {
            val errorCode = TossPaymentErrorCode.fromCode(it)
            throw PurchaseException(PurchaseExceptionCode.FAILED, errorCode.message)
        }
    }

    private fun getCustomHeader(): String? {
        val currentRequest = (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)?.request
        return currentRequest?.getHeader("Payment-Error-Header")
    }

    private fun simulateDelay() {
        val delayMillis = Random.nextLong(100, 500)
        Thread.sleep(delayMillis)
    }
}

