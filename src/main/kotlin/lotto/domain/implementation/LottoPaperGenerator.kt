package lotto.domain.implementation

import common.business.Implementation
import lotto.domain.entity.IssueStatus
import lotto.domain.entity.IssuedLotto
import lotto.domain.entity.Lotto
import lotto.domain.repository.LottoRepository
import lotto.domain.vo.LottoNumbers
import lotto.domain.vo.LottoPaper
import lotto.domain.vo.LottoPaperRequest
import java.math.BigDecimal

@Implementation
class LottoPaperGenerator(
    private val lottoNumberGenerator: LottoNumberGenerator,
    private val lottoRepository: LottoRepository
) {
    companion object {
        private val UNIT = BigDecimal(1000L)
    }

    fun generateWithAuto(lottoPaperRequest: LottoPaperRequest): LottoPaper {
        val paperCount = calculatePaperCount(lottoPaperRequest)
        val issuedLottoes = createIssuedLottoes(lottoNumberGenerator.generate(paperCount), IssueStatus.AUTO)
        return LottoPaper(issuedLottoes)
    }

    fun generateWithNumbers(lottoPaperRequest: LottoPaperRequest, lottoNumbers: LottoNumbers): LottoPaper {
        val paperCount = calculatePaperCount(lottoPaperRequest)
        require(paperCount == lottoNumbers.size()) {
            "로또 숫자와 개수는 일치해야 합니다."
        }
        val issuedLottoes = createIssuedLottoes(lottoNumbers, IssueStatus.MANUAL)
        return LottoPaper(issuedLottoes)
    }

    private fun calculatePaperCount(lottoPaperRequest: LottoPaperRequest): Int {
        require(lottoPaperRequest.isDivide(UNIT)) {
            "금액은 ${UNIT}원 단위여야 합니다."
        }
        return lottoPaperRequest.amount.divide(UNIT).toInt()
    }

    private fun createIssuedLottoes(lottoNumbers: LottoNumbers, issueStatus: IssueStatus): List<IssuedLotto> {
        return getLottoEntities(lottoNumbers).map { IssuedLotto(issueStatus, it) }.toList()
    }

    private fun getLottoEntities(lottoNumbers: LottoNumbers): List<Lotto> {
        return lottoRepository.findLottoByNumbersIn(lottoNumbers.toStringWithComma())
    }
}
