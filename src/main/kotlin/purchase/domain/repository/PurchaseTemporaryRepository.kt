package purchase.domain.repository

import org.springframework.data.repository.CrudRepository
import purchase.domain.entity.PurchaseTemporary
import java.util.*

interface PurchaseTemporaryRepository : CrudRepository<PurchaseTemporary, UUID> {
    fun findByOrderId(orderId: String): Optional<PurchaseTemporary>
}
