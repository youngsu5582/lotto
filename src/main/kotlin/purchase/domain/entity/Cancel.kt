package purchase.domain.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import purchase.domain.vo.PurchaseProvider
import java.time.LocalDateTime
import java.util.*

@Entity
class Cancel(
    @Id
    private val id: UUID? = UUID.randomUUID(),
    private val orderId: String,
    private val paymentKey: String,
    private val cancelAt: LocalDateTime,
    private val cancelAmount: Long,
    private val cancelReason: String,
    @Enumerated(EnumType.STRING)
    private val purchaseProvider: PurchaseProvider,
)

