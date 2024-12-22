package toss.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonIgnoreProperties(ignoreUnknown = true)
data class TossPaymentResponse(
    val paymentKey: String,
    val status: String,
    val orderId: String,
    val totalAmount: Long,
    @JsonDeserialize(using = PaymentMethodDeserializer::class)
    val method: PaymentMethod
)

enum class PaymentMethod(private val ln: List<String>) {
    CARD(listOf("CARD", "카드","Tarjeta")),
    CASH(listOf("CASH", "현금","Efectivo")),
    SIMPLE_PAY(listOf("SIMPLE_PAY","간편결제")),;

    companion object {
        private val IDENTIFY: Map<String, PaymentMethod> =
            entries.flatMap { method ->
                method.ln.map { alias -> alias.uppercase() to method }
            }.toMap()

        fun get(s: String) = IDENTIFY[s.uppercase()]
    }
}

class PaymentMethodDeserializer : JsonDeserializer<PaymentMethod>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): PaymentMethod {
        return PaymentMethod.get(p.text) ?: throw NoSuchElementException("메소드에 해당하는 값이 없습니다")
    }
}
