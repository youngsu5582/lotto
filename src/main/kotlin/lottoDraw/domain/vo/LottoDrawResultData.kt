package lottoDraw.domain.vo

import lotto.domain.vo.LottoNumber
import lotto.domain.vo.LottoNumbers
import java.time.LocalDate

class LottoDrawResultData(
    val round: Int,

    val date: LocalDate,
    val numbers: LottoNumbers,
    val bonus: LottoNumber,

    val matchCount: Int,
    val bonusMatch: Boolean
) {
    companion object {
        fun from(lottoDraw: LottoDraw, lottoResult: LottoResult): LottoDrawResultData {
            return LottoDrawResultData(
                round = lottoDraw.round,
                date = lottoDraw.date,
                numbers = lottoDraw.numbers,
                bonus = lottoDraw.bonus,
                matchCount = lottoResult.matchCount,
                bonusMatch = lottoResult.bonusMatch
            )
        }
    }
}
