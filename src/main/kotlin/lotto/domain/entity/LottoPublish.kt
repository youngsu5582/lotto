package lotto.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class LottoPublish(
    @Id
    @GeneratedValue
    private val id: Long? = null,
    @ManyToOne(fetch = FetchType.EAGER)
    private val lottoRoundInfo: LottoRoundInfo,
    private val issuedAt: LocalDateTime,
    @Enumerated(EnumType.STRING)
    private var status: LottoPublishStatus,
) {
    fun getId() = id ?: throw IllegalArgumentException("Not Exist Id")
    fun getLottoRoundInfo() = lottoRoundInfo
    fun getIssuedAt() = issuedAt
    fun status() = status.name
    fun isCancel() = status == LottoPublishStatus.CANCELED
    fun isStatus(status: LottoPublishStatus) = this.status == status

    fun cancel() {
        if (this.status != LottoPublishStatus.COMPLETE) {
            throw IllegalStateException("결제 완료 상태에서만 취소가 가능합니다")
        }
        this.status = LottoPublishStatus.CANCELED
    }

    fun complete() {
        if (this.status != LottoPublishStatus.PENDING) {
            throw IllegalStateException("결제 승인 상태에서만 완료가 가능합니다")
        }
        this.status = LottoPublishStatus.COMPLETE
    }

    fun pending() {
        if (this.status != LottoPublishStatus.WAITING) {
            throw IllegalStateException("결제 대기 상태에서만 완료가 가능합니다")
        }
        this.status = LottoPublishStatus.PENDING
    }

    fun waiting() {
        if (this.status != LottoPublishStatus.PENDING) {
            throw IllegalStateException("결제 진행 대기 상태에서만 완료가 가능합니다")
        }
        this.status = LottoPublishStatus.WAITING
    }
}

/**
 * CANCELED : 취소된 상태
 * WAITING : 결제 대기중인 상태
 * PENDING : 결제 승인 대기중인 상태
 * COMPLETE : 결제 완료된 상태
 */
enum class LottoPublishStatus {
    CANCELED, COMPLETE, WAITING, PENDING
}
