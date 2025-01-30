package purchase.domain.entity

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import jakarta.persistence.*
import purchase.domain.PaymentMethodDeserializer
import purchase.domain.vo.PaymentMethod
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

@Entity
class PurchaseInfo(
    @Id
    @GeneratedValue(generator = "UUID")
    private val id: UUID? = null,
    private val totalAmount: BigDecimal,
    @JsonDeserialize(using = PaymentMethodDeserializer::class)
    @Enumerated(EnumType.STRING)
    private val method: PaymentMethod,
) {
    fun getId() = id
    fun getTotalAmount() = totalAmount
    fun getMethod() = method
    fun isEqual(amount: BigDecimal): Boolean {
        require(amount >= BigDecimal.ZERO) { "금액은 0원 이상이여야 합니다." }
        return totalAmount.setScale(2, RoundingMode.HALF_UP) ==
                amount.setScale(2, RoundingMode.HALF_UP)
    }
}
