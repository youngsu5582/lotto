package lotto.domain.lotto

import common.business.Implementation
import lotto.domain.lotto.vo.LottoNumbers
import lotto.domain.lotto.vo.LottoPaper
import java.math.BigDecimal

@Implementation
class LottoPaperGenerator(
    private val lottoNumberGenerator: LottoNumberGenerator
) {
    companion object {
        private val UNIT = BigDecimal(1000L)
    }

    fun generateWithAuto(lottoPaperRequest: LottoPaperRequest): LottoPaper {
        require(lottoPaperRequest.isDivide(UNIT)) {
            "금액은 ${UNIT}원 단위여야 합니다."
        }
        val paperCount = lottoPaperRequest.amount.divide(UNIT).toInt()
        return LottoPaper(lottoNumberGenerator.generate(paperCount))
    }

    fun generateWithNumbers(lottoPaperRequest: LottoPaperRequest, lottoNumbers: LottoNumbers): LottoPaper {
        val paperCount = calculatePaperCount(lottoPaperRequest)
        require(paperCount == lottoNumbers.size()) {
            "로또 숫자와 개수는 일치해야 합니다."
        }
        val issuedLottoes = lottoNumbers.stream()
            .map {
                IssuedLotto(
                    IssueStatus.MANUAL,
                    Lotto(it)
                )
            }.toList()

        return LottoPaper(issuedLottoes)
    }

    private fun calculatePaperCount(lottoPaperRequest: LottoPaperRequest): Int {
        require(lottoPaperRequest.isDivide(UNIT)) {
            "금액은 ${UNIT}원 단위여야 합니다."
        }
        return lottoPaperRequest.amount.divide(UNIT).toInt()
    }
}

data class LottoPaperRequest(
    val amount: BigDecimal
) {
    fun isDivide(unit: BigDecimal): Boolean {
        return amount.remainder(unit) == BigDecimal.ZERO
    }
}
