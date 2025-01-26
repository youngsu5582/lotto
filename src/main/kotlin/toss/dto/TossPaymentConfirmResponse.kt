package toss.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import purchase.domain.PaymentMethodDeserializer
import purchase.domain.vo.PaymentMethod
import purchase.domain.vo.PurchaseData
import purchase.domain.vo.PurchaseProvider
import purchase.domain.vo.PurchaseStatus
import java.math.BigDecimal

@JsonIgnoreProperties(ignoreUnknown = true)
data class TossPaymentConfirmResponse(
    val paymentKey: String = "",
    val status: String = "",
    val orderId: String = "",
    val totalAmount: Long = 0,
    @JsonDeserialize(using = PaymentMethodDeserializer::class)
    val method: PaymentMethod,
) {
    fun toPurchaseData(): PurchaseData {
        return PurchaseData(
            paymentKey = paymentKey,
            status = convertStatus(status),
            method = method,
            purchaseProvider = PurchaseProvider.TOSS,
            orderId = orderId,
            totalAmount = BigDecimal(totalAmount),
        )
    }

    private fun convertStatus(status: String): PurchaseStatus {
        return when (status) {
            "DONE" -> PurchaseStatus.SUCCESS
            else -> PurchaseStatus.FAIL
        }
    }
}
