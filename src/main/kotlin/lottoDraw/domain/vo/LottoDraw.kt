package lottoDraw.domain.vo

import lotto.domain.vo.LottoNumber
import lotto.domain.vo.LottoNumbers
import java.time.LocalDate

data class LottoDraw(
    val round: Int,
    val date: LocalDate,
    val numbers: LottoNumbers,
    val bonus: LottoNumber
) {
    fun matchResult(numbers: LottoNumbers): LottoResult {
        val count = numberCheck(numbers)
        val bonus = bonusCheck(numbers)

        return LottoResult(
            round = round,
            matchCount = count,
            bonusMatch = bonus
        )
    }

    private fun bonusCheck(numbers: LottoNumbers): Boolean = numbers.contains(bonus)
    private fun numberCheck(numbers: LottoNumbers): Int = this.numbers.check(numbers)
}
