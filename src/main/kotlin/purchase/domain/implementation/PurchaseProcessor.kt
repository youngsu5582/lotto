package purchase.domain.implementation

import common.business.Implementation
import purchase.domain.entity.Purchase
import purchase.domain.vo.CancelRequest
import purchase.domain.vo.PurchaseProvider
import purchase.domain.vo.PurchaseRequest

@Implementation
class PurchaseProcessor(
    private val paymentProcessor: PaymentProcessor,
    private val purchaseValidator: PurchaseValidator,
    private val purchaseWriter: PurchaseWriter,
) {
    companion object {
        const val DEFAULT_REASON = "단순 변심"
    }

    fun purchase(purchaseRequest: PurchaseRequest): Purchase {
        val purchaseData = paymentProcessor.purchase(purchaseRequest, PurchaseProvider.TOSS)
        val purchase = purchaseWriter.savePurchase(purchaseData)
        return purchase
    }

    fun cancel(purchase: Purchase, provider: PurchaseProvider = PurchaseProvider.TOSS): Purchase {
        purchaseValidator.checkCancelValid(purchase)
        val cancelData = paymentProcessor.cancel(purchase.toCancelRequest(), provider)
        return purchaseWriter.cancelPurchase(cancelData, purchase)
    }

    private fun Purchase.toCancelRequest(): CancelRequest {
        return CancelRequest(
            paymentKey = this.getPaymentKey(),
            cancelReason = DEFAULT_REASON,
            orderId = this.getOrderId(),
            amount = this.getTotalAmount()
        )
    }
}
