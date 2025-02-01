package lotto.domain.vo

import order.domain.implementation.OrderDataRequest
import purchase.domain.vo.PurchaseRequest
import java.math.BigDecimal

data class LottoPurchaseRequest(
    val purchaseType: PurchaseType,
    val currency: Currency,
    val amount: BigDecimal,
    val paymentKey: String,
    val orderId: String,
) {
    fun toPurchaseRequest(): PurchaseRequest {
        return PurchaseRequest(
            purchaseType = purchaseType.name,
            amount = amount,
            currency = currency.name,
            paymentKey = paymentKey,
            orderId = orderId,
        )
    }
    fun toOrderDataRequest(): OrderDataRequest {
        return OrderDataRequest(
            amount = amount,
            orderId = orderId,
        )
    }
}
