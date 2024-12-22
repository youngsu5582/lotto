package lotto.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    indexes = [
        Index(name = "idx_issue_end_date", columnList = "startDate, endDate")
    ]
)
data class LottoRoundInfo(
    @Id
    @GeneratedValue
    val id: Long? = null,

    val round: Long,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val drawDate: LocalDateTime,
    val paymentDeadline: LocalDateTime,
    @Enumerated(EnumType.STRING)
    val status: LottoStatus = LottoStatus.ONGOING,
) {

    fun isOngoing(): Boolean = status == LottoStatus.ONGOING
    fun isNotOngoing(): Boolean = status != LottoStatus.ONGOING
    fun isAfter(localDateTime: LocalDateTime): Boolean = localDateTime.isAfter(endDate)
}

enum class LottoStatus {
    ONGOING, COMPLETED, CLOSED
}
