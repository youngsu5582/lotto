package toss.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import purchase.domain.PaymentMethodDeserializer
import purchase.domain.vo.PaymentMethod
import purchase.domain.vo.PurchaseData
import purchase.domain.vo.PurchaseProvider
import java.math.BigDecimal

@JsonIgnoreProperties(ignoreUnknown = true)
data class TossPaymentResponse(
    val paymentKey: String="",
    val status: String="",
    val orderId: String="",
    val totalAmount: Long=0,
    @JsonDeserialize(using = PaymentMethodDeserializer::class)
    val method: PaymentMethod,
) {
    fun toPurchaseData(): PurchaseData {
        return PurchaseData(
            paymentKey = paymentKey,
            status = status,
            method = method,
            purchaseProvider = PurchaseProvider.TOSS,
            orderId = orderId,
            totalAmount = BigDecimal(totalAmount),
        )
    }
}
