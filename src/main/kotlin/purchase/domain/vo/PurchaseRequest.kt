package purchase.domain.vo

import java.math.BigDecimal

data class PurchaseRequest(
    val purchaseType: String,
    val currency: String,
    val amount: BigDecimal,
    val paymentKey: String,
    val orderId: String,
)
