package purchase.domain.vo

import java.math.BigDecimal

data class PurchaseData(
    val paymentKey: String,
    val orderId: String,
    val status: String,
    val purchaseProvider: PurchaseProvider,
    val method: PaymentMethod,
    val totalAmount: BigDecimal,
)
