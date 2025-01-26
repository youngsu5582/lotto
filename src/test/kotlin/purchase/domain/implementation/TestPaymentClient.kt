package purchase.domain.implementation

import purchase.domain.PaymentClient
import purchase.domain.vo.*
import java.time.LocalDateTime

class TestPaymentClient : PaymentClient {
    override fun process(request: PurchaseRequest): PurchaseData {
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
}
