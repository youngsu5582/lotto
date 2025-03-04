package lotto.controller

import lotto.service.dto.LottoStatisticsData

data class LottoStaticHttpResponse(
    val lottoRoundInfoId: Long,
    val memberCount: Int,
    val lottoPublishCount: Int,
    val totalPurchaseMoney: Long
) {
    companion object {
        fun from(lottoStatisticsData: LottoStatisticsData) = with(lottoStatisticsData) {
            LottoStaticHttpResponse(
                lottoRoundInfoId = lottoRoundInfoId,
                memberCount = memberCount,
                lottoPublishCount = lottoPublishCount,
                totalPurchaseMoney = totalPurchaseMoney
            )
        }
    }
}
