package toss.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import purchase.domain.PaymentMethodDeserializer
import purchase.domain.vo.PaymentMethod

@JsonIgnoreProperties(ignoreUnknown = true)
data class TossPaymentResponse(
    val paymentKey: String,
    val status: String,
    val orderId: String,
    val totalAmount: Long,
    @JsonDeserialize(using = PaymentMethodDeserializer::class)
    val method: PaymentMethod,
)
