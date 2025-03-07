package lotto.domain.entity

import jakarta.persistence.*

@Entity
@Table(
    indexes = [
        Index(name = "idx_numbers", columnList = "numbers"),
    ],
)
class LottoBill(
    @Id
    @GeneratedValue
    private val id: Long? = null,
    private val purchaseId: String,
    private val lottoPublishId: Long,
    private val memberId: String,
    private val lottoRoundInfoId: Long
) {
    fun getId() = id
    fun getPurchaseId() = purchaseId
    fun getLottoPublishId() = lottoPublishId
    fun getMemberId() = memberId
    fun isOwner(memberId: String) = this.memberId == memberId
    fun getLottoRoundInfoId() = lottoRoundInfoId
}
