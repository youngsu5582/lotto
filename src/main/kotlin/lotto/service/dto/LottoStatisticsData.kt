package lotto.service.dto

import lotto.domain.entity.LottoStatistics
import java.time.LocalDateTime

data class LottoStatisticsData(
    val lottoRoundInfoId: Long,
    val memberCount: Int,
    val lottoPublishCount: Int,
    val totalPurchaseMoney: Long,
    val updatedAt: LocalDateTime
) {
    companion object {
        @JvmStatic
        fun from(lottoStatistics: LottoStatistics): LottoStatisticsData {
            return with(lottoStatistics) {
                LottoStatisticsData(
                    lottoRoundInfoId = lottoRoundInfoId,
                    memberCount = memberCount,
                    lottoPublishCount = lottoPublishCount,
                    totalPurchaseMoney = totalPurchaseMoney.toLong(),
                    updatedAt = updatedAt
                )
            }
        }
    }
}
