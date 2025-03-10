package lottoDraw.service.dto

import lottoDraw.domain.vo.LottoResult

data class LottoResultData(
    val round: Int,
    val matchCount: Int,
    val bonusMatch: Boolean
) {
    companion object {
        @JvmStatic
        fun from(lottoNumberCount: LottoResult): LottoResultData {
            return LottoResultData(
                round = lottoNumberCount.round,
                matchCount = lottoNumberCount.matchCount,
                bonusMatch = lottoNumberCount.bonusMatch
            )
        }
    }
}
