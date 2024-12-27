package purchase.domain.entity

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import jakarta.persistence.Entity
import jakarta.persistence.Id
import purchase.domain.PaymentMethodDeserializer
import purchase.domain.vo.PaymentMethod
import java.math.BigDecimal
import java.util.*

@Entity
class PurchaseInfo(
    @Id
    private val id: UUID? = UUID.randomUUID(),
    private val status: String,
    private val totalAmount: BigDecimal,
    @JsonDeserialize(using = PaymentMethodDeserializer::class)
    private val method: PaymentMethod,
)
