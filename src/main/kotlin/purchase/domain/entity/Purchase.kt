package purchase.domain.entity

import jakarta.persistence.*
import org.hibernate.annotations.UpdateTimestamp
import purchase.domain.vo.PurchaseProvider
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity
class Purchase(
    @Id
    @GeneratedValue(generator = "UUID")
    private val id: UUID? = null,
    private val orderId: String,
    private val paymentKey: String,
    @OneToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "purchase_info_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private val purchaseInfo: PurchaseInfo,
    private var status: String,

    @Enumerated(EnumType.STRING)
    private val purchaseProvider: PurchaseProvider,

    private val createdAt: LocalDateTime = LocalDateTime.now(),
    @UpdateTimestamp
    private val updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    fun getId() = id ?: throw IllegalArgumentException("Not Exist Id")
    fun getOrderId() = orderId
    fun getPaymentKey() = paymentKey
    fun getPurchaseProvider() = purchaseProvider
    fun getTotalAmount() = purchaseInfo.getTotalAmount()
    fun getStatus() = status
    fun getMethod() = purchaseInfo.getMethod()

    fun isSuccess() = status == "SUCCESS"
    fun isCanceled() = status == "CANCELED"
    fun isNotSuccess() = !isSuccess()
    fun isNotEqual(amount: BigDecimal) = !purchaseInfo.isEqual(amount)
    fun cancel() = run {
        if (this.status == "CANCELED") {
            throw IllegalArgumentException("이미 취소된 결제입니다.")
        }
        this.status = "CANCELED"
    }
}
