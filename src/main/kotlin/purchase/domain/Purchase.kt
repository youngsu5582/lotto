package purchase.domain

import jakarta.persistence.*
import java.util.*

@Entity
class Purchase(
    @Id
    private val id: UUID? = UUID.randomUUID(),
    private val orderId: String,
    private val paymentKey: String,

    @OneToOne(cascade = [CascadeType.PERSIST])
    private val purchaseInfo: PurchaseInfo,

    @Enumerated
    private val purchaseProvider: PurchaseProvider
) {
}

enum class PurchaseProvider {
    TOSS
}
