package lotto.domain.implementation

import common.business.Implementation
import purchase.domain.implementation.PaymentProcessor
import purchase.domain.entity.Purchase
import purchase.domain.implementation.PurchaseWriter
import purchase.domain.vo.PurchaseProvider
import purchase.domain.vo.PurchaseRequest

@Implementation
class LottoPurchaseProcessor(
    private val paymentProcessor: PaymentProcessor,
    private val purchaseWriter: PurchaseWriter
) {
    fun purchase(purchaseRequest: PurchaseRequest): Purchase {
        val response = paymentProcessor.purchase(purchaseRequest, PurchaseProvider.TOSS)
        val purchase = purchaseWriter.savePurchase(response)
        return purchase
    }
}
