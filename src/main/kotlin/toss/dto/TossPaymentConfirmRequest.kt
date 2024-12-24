package toss.dto

data class TossPaymentConfirmRequest(
    val amount: Long,
    val paymentKey: String,
    val orderId: String
)

data class TossPaymentCancelRequest(
    val cancelReason: String
)
