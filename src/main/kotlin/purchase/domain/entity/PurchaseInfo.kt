package purchase.domain.entity

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import purchase.domain.PaymentMethodDeserializer
import purchase.domain.vo.PaymentMethod
import java.math.BigDecimal
import java.util.*

@Entity
class PurchaseInfo(
    @Id
    private val id: UUID? = UUID.randomUUID(),
    private val totalAmount: BigDecimal,
    @JsonDeserialize(using = PaymentMethodDeserializer::class)
    @Enumerated(EnumType.STRING)
    private val method: PaymentMethod,
) {
    fun getId() = id
    fun getTotalAmount() = totalAmount
    fun getMethod() = method
}
