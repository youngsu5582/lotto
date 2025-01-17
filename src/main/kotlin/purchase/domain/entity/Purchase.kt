package purchase.domain.entity

import jakarta.persistence.*
import purchase.domain.vo.PurchaseProvider
import java.util.*

@Entity
class Purchase(
    @Id
    private val id: UUID? = UUID.randomUUID(),
    private val orderId: String,
    private val paymentKey: String,
    @OneToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "purchase_info_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private val purchaseInfo: PurchaseInfo,
    private val status: String,

    @Enumerated(EnumType.STRING)
    private val purchaseProvider: PurchaseProvider,
) {
    fun getId() = id
    fun getOrderId() = orderId
    fun getPaymentKey() = paymentKey
    fun getPurchaseProvider() = purchaseProvider
    fun getTotalAmount() = purchaseInfo.getTotalAmount()
    fun getStatus() = status
    fun getMethod() = purchaseInfo.getMethod()

    fun isSuccess() = status == "SUCCESS"
}
