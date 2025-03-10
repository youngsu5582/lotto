package lottoDraw.domain.vo

import lotto.domain.vo.LottoNumbers

data class LottoDraws(
    val data: List<LottoDraw>
) {
    fun matchResult(lottoNumbers: LottoNumbers): List<LottoDrawResultData> {
        return data.map {
            val result = it.matchResult(lottoNumbers)
            LottoDrawResultData.from(
                lottoDraw = it,
                lottoResult = result
            )
        }.sortedBy { it.round }
    }
}
