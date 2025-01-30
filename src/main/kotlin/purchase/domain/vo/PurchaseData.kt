package purchase.domain.vo

import java.math.BigDecimal

data class PurchaseData(
    val paymentKey: String,
    val orderId: String,
    val status: PurchaseStatus,
    val purchaseProvider: PurchaseProvider,
    val method: PaymentMethod,
    val totalAmount: BigDecimal,
) {
    fun isSuccess() = status == PurchaseStatus.SUCCESS
    fun status(): String = status.name
}

enum class PurchaseStatus(
    val status: String
) {
    SUCCESS("SUCCESS"),
    FAIL("FAIL"),
    CANCELED("CANCELED"),
    READY("READY"),
    DONE("DONE"),
    IN_PROGRESS("IN_PROGRESS")
}
