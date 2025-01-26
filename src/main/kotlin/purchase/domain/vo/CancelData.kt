package purchase.domain.vo

import java.time.LocalDateTime

data class CancelData(
    val orderId: String,
    val paymentKey: String,
    val cancels: List<Cancel>,
    val status: CancelStatus,
    val purchaseProvider: PurchaseProvider,
) {
    fun isCanceled() = status == CancelStatus.CANCELED
}

data class Cancel(
    val transactionKey: String,
    val cancelAt: LocalDateTime,
    val cancelAmount: Long,
    val cancelReason: String,
)

enum class CancelStatus(
    val status: String
) {
    CANCELED("CANCELED"),
    FAIL("FAIL"),
}
