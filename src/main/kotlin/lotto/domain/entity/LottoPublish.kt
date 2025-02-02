package lotto.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class LottoPublish(
    @Id
    @GeneratedValue
    private val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    private val lottoRoundInfo: LottoRoundInfo,
    private val issuedAt: LocalDateTime,
    private var canceled: Boolean = false,
) {
    fun getId() = id ?: throw IllegalArgumentException("Not Exist Id")
    fun getLottoRoundInfo() = lottoRoundInfo
    fun getIssuedAt() = issuedAt
    fun getCanceled() = canceled
    fun cancel() {
        this.canceled = true
    }
}
