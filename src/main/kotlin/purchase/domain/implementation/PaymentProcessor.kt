package purchase.domain.implementation

import common.business.Implementation
import jakarta.transaction.NotSupportedException
import purchase.domain.PaymentClient
import purchase.domain.vo.*

@Implementation
class PaymentProcessor(
    private val paymentClients: Map<String, PaymentClient>,
) {
    fun purchase(
        purchaseRequest: PurchaseRequest,
        purchaseProvider: PurchaseProvider,
    ): PurchaseData {
        val client = getPurchaseClient(purchaseProvider)
        return client.process(request = purchaseRequest)
    }

    fun cancel(
        cancelRequest: CancelRequest,
        purchaseProvider: PurchaseProvider,
    ): CancelData {
        val client = getPurchaseClient(purchaseProvider)
        return client.cancel(request = cancelRequest)
    }

    private fun getPurchaseClient(purchaseProvider: PurchaseProvider): PaymentClient {
        return paymentClients[purchaseProvider.name]
            ?: throw NotSupportedException("지원하지 않는 결제 제공자입니다.")
    }
}
