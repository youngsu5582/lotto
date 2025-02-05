package purchase.domain.repository

import org.springframework.data.repository.CrudRepository
import purchase.domain.entity.Purchase
import java.util.*

interface PurchaseRepository : CrudRepository<Purchase, UUID> {
    fun findByPaymentKey(paymentKey: String): Optional<Purchase>
    fun findByOrderId(orderId: String): Optional<Purchase>
}
