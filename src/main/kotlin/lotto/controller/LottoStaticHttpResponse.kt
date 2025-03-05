package lotto.controller

import lotto.service.dto.LottoStatisticsData
import java.time.LocalDateTime

data class LottoStaticHttpResponse(
    val lottoRoundInfo: Long,
    val memberCount: Int,
    val lottoPublishCount: Int,
    val totalPurchaseMoney: Long,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(lottoStatisticsData: LottoStatisticsData) = with(lottoStatisticsData) {
            LottoStaticHttpResponse(
                lottoRoundInfo = round,
                memberCount = memberCount,
                lottoPublishCount = lottoPublishCount,
                totalPurchaseMoney = totalPurchaseMoney,
                updatedAt = updatedAt
            )
        }
    }
}
