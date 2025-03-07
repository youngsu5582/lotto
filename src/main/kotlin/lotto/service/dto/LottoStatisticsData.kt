package lotto.service.dto

import lotto.domain.entity.LottoStatistics
import java.time.LocalDateTime

data class LottoStatisticsData(
    val round: Long,
    val memberCount: Int,
    val lottoPublishCount: Int,
    val totalPurchaseMoney: Long,
    val updatedAt: LocalDateTime
) {
    companion object {
        @JvmStatic
        fun from(lottoStatistics: LottoStatistics, round: Long): LottoStatisticsData {
            return with(lottoStatistics) {
                LottoStatisticsData(
                    round = round,
                    memberCount = memberCount,
                    lottoPublishCount = lottoPublishCount,
                    totalPurchaseMoney = totalPurchaseMoney.toLong(),
                    updatedAt = updatedAt
                )
            }
        }
    }
}
