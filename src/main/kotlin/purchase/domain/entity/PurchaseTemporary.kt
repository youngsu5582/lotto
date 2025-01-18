package purchase.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.math.BigDecimal

@Entity
class PurchaseTemporary(
    @Id
    @Column(unique = true)
    private val orderId: String,
    private val amount: BigDecimal,
) {
    fun getOrderId() = orderId
    fun getAmount() = amount
    fun isNotEqualAmount(amount: BigDecimal, scale: Int = 0) = this.amount.setScale(scale) != amount.setScale(scale)
}
