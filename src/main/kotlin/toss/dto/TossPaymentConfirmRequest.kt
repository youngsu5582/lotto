package toss.dto

import java.math.BigDecimal

data class TossPaymentConfirmRequest(
    val amount: BigDecimal,
    val paymentKey: String,
    val orderId: String
)

data class TossPaymentCancelRequest(
    val cancelReason: String
)
