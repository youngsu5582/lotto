package purchase.domain.vo

import java.math.BigDecimal

data class CancelRequest(
    val paymentKey: String,
    val orderId: String,
    val cancelReason: String,
    val amount: BigDecimal
) {
}
