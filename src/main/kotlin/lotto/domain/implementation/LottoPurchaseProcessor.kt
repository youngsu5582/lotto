package lotto.domain.implementation

import common.business.Implementation
import lotto.domain.vo.LottoPurchaseRequest
import purchase.domain.entity.Purchase
import purchase.domain.implementation.OrderDataProcessor
import purchase.domain.implementation.OrderDataRequest
import purchase.domain.implementation.PaymentProcessor
import purchase.domain.implementation.PurchaseWriter
import purchase.domain.vo.PurchaseProvider

@Implementation
class LottoPurchaseProcessor(
    private val paymentProcessor: PaymentProcessor,
    private val orderDataProcessor: OrderDataProcessor,
    private val purchaseWriter: PurchaseWriter,
) {
    fun purchase(lottoPurchaseRequest: LottoPurchaseRequest): Purchase {
        orderDataProcessor.checkOrderValid(request = convertRequest(lottoPurchaseRequest))
        val response = paymentProcessor.purchase(lottoPurchaseRequest.toPurchaseRequest(), PurchaseProvider.TOSS)
        val purchase = purchaseWriter.savePurchase(response)
        return purchase
    }

    private fun convertRequest(lottoPurchaseRequest: LottoPurchaseRequest): OrderDataRequest {
        return OrderDataRequest(
            amount = lottoPurchaseRequest.amount,
            orderId = lottoPurchaseRequest.orderId,
        )
    }
}
