package purchase.domain.implementation

import common.business.Implementation
import io.github.oshai.kotlinlogging.KotlinLogging

import purchase.domain.entity.Purchase
import purchase.domain.vo.CancelRequest
import purchase.domain.vo.PurchaseProvider
import purchase.domain.vo.PurchaseRequest

private val log = KotlinLogging.logger {}
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
        log.info { "결제가 완료됐습니다. 결제 키 : ${purchaseData.paymentKey}, ${purchaseData.status}, ${purchaseData.totalAmount}" }
        return purchase
    }

    fun cancel(purchaseId: String, provider: PurchaseProvider = PurchaseProvider.TOSS): Purchase {
        val purchase = purchaseValidator.checkCancelValid(purchaseId)
        val cancelData = paymentProcessor.cancel(purchase.toCancelRequest(), provider)
        log.info { "결제가 취소됐습니다. 결제 키 : ${cancelData.paymentKey}, ${cancelData.status}, ${cancelData.cancels.sumOf { it.cancelAmount }}" }
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
