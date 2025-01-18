package purchase.domain.implementation

import purchase.domain.PaymentClient
import purchase.domain.vo.PaymentMethod
import purchase.domain.vo.PurchaseData
import purchase.domain.vo.PurchaseProvider
import purchase.domain.vo.PurchaseRequest

class TestPaymentClient : PaymentClient {
    override fun process(request: PurchaseRequest): PurchaseData {
        return PurchaseData(
            totalAmount = request.amount,
            paymentKey = request.paymentKey,
            orderId = request.orderId,
            status = "SUCCESS",
            purchaseProvider = PurchaseProvider.TOSS,
            method = PaymentMethod.CARD,
        )
    }
}
