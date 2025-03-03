package purchase.domain.implementation

import common.business.Implementation
import purchase.domain.PaymentClient
import purchase.domain.vo.*

@Implementation
class PaymentProcessor(
    private val paymentClients: List<PaymentClient>,
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
        return paymentClients.firstOrNull() { it.support(purchaseProvider.name) }
            ?: throw IllegalArgumentException("지원하지 않는 결제 제공자입니다.")
    }
}
