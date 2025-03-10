package purchase.domain.implementation

import purchase.domain.PaymentClient
import purchase.domain.vo.*
import java.time.LocalDateTime
import kotlin.random.Random

class TestPaymentClient : PaymentClient {
    override fun process(request: PurchaseRequest): PurchaseData {
        simulateDelay()
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

    private fun simulateDelay() {
        val delayMillis = Random.nextLong(100, 500)
        Thread.sleep(delayMillis)
    }
}
