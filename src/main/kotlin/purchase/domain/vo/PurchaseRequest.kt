package purchase.domain.vo

import purchase.domain.implementation.OrderDataRequest
import java.math.BigDecimal

data class PurchaseRequest(
    val purchaseType: String,
    val currency: String,
    val amount: BigDecimal,
    val paymentKey: String,
    val orderId: String,
) {
    fun toOrderDataRequest(): OrderDataRequest {
        return OrderDataRequest(
            amount = amount,
            orderId = orderId,
        )
    }
}
