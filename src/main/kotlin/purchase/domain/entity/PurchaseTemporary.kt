package purchase.domain.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.math.BigDecimal
import java.util.*

@Entity
class PurchaseTemporary(
    @Id
    private val id: UUID? = UUID.randomUUID(),
    private val orderId: String,
    private val amount: BigDecimal,
) {
    fun getId() = id
    fun getOrderId() = orderId
    fun getAmount() = amount
    fun isNotEqualAmount(amount: BigDecimal,scale:Int=0) = this.amount.setScale(scale) != amount.setScale(scale)
}
