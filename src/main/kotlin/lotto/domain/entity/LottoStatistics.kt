package lotto.domain.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
data class LottoStatistics(
    @Id
    val lottoRoundInfoId: Long,
    val memberCount: Int,
    val lottoPublishCount: Int,
    val totalPurchaseMoney: BigDecimal,
    val updatedAt: LocalDateTime,
)
