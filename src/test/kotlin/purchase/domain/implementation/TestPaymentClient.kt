package purchase.domain.implementation

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import purchase.domain.PaymentClient
import purchase.domain.PurchaseException
import purchase.domain.PurchaseExceptionCode
import purchase.domain.vo.*
import toss.TossPaymentErrorCode
import java.time.LocalDateTime

class TestPaymentClient : PaymentClient {
    override fun process(request: PurchaseRequest): PurchaseData {
        ifCustomHeaderThrowException()
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
}
