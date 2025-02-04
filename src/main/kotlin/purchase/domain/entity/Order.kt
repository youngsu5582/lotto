package purchase.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "orders")
class Order(
    @Id
    @Column(unique = true)
    private val orderId: String,
    private val amount: BigDecimal,
) {
    fun getOrderId() = orderId
    fun getAmount() = amount
    fun isNotEqualAmount(amount: BigDecimal, scale: Int = 0) = this.amount.setScale(scale) != amount.setScale(scale)
}
