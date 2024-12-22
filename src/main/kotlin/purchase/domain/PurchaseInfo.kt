package purchase.domain

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import jakarta.persistence.Entity
import jakarta.persistence.Id
import toss.dto.PaymentMethod
import toss.dto.PaymentMethodDeserializer
import java.util.*

@Entity
class PurchaseInfo(
    @Id
    private val id: UUID? = UUID.randomUUID(),

    private val status: String,

    private val totalAmount: Long,
    @JsonDeserialize(using = PaymentMethodDeserializer::class)
    private val method: PaymentMethod
)
