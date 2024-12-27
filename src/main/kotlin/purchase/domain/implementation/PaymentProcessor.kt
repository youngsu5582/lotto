package purchase.domain.implementation

import common.business.Implementation
import jakarta.transaction.NotSupportedException
import purchase.domain.PaymentClient
import purchase.domain.vo.PurchaseData
import purchase.domain.vo.PurchaseProvider
import purchase.domain.vo.PurchaseRequest

@Implementation
class PaymentProcessor(
    private val paymentClients: Map<PurchaseProvider, PaymentClient>,
) {
    fun purchase(
        purchaseRequest: PurchaseRequest,
        purchaseProvider: PurchaseProvider,
    ): PurchaseData {
        val client = getPurchaseClient(purchaseProvider)
        return client.process(request = purchaseRequest)
    }

    private fun getPurchaseClient(purchaseProvider: PurchaseProvider): PaymentClient {
        return paymentClients[purchaseProvider]
            ?: throw NotSupportedException("지원하지 않는 결제 제공자입니다.")
    }
}
