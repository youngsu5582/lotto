package lotto.domain.lotto.vo

import java.math.BigDecimal

data class PurchaseRequest(
    val purchaseType: PurchaseType,
    val currency: Currency,
    val amount: BigDecimal,
    val paymentKey: String,
    val orderId: String
)

enum class PurchaseType {
    CARD,
    CASH
}

enum class Currency {
    KRW
}
