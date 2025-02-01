package lotto.domain.implementation

import common.business.Implementation
import common.business.Read
import common.business.Transaction
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
    private val lottoRepository: LottoRepository,
) {
    companion object {
        private val UNIT = BigDecimal(1000L)
    }

    @Transaction
    @Read
    fun generateWithNumbers(
        lottoNumbers: LottoNumbers,
    ): LottoPaper {
        val issuedLottoes = createIssuedLottoes(lottoNumbers, IssueStatus.MANUAL)
        val amount = calculateAmount(lottoNumbers)
        return LottoPaper(lottoes = issuedLottoes, amount = amount)
    }

    private fun calculateAmount(lottoNumbers: LottoNumbers): BigDecimal {
        return lottoNumbers.calculatePrice(UNIT)
    }

    /**
     * 당장 서버 내부에서 자체 생성해주는 로또는 보류한다.
     * 2025.02.02
     */
//    @Transaction
//    @Read
//    fun generateWithAuto(lottoPaperRequest: LottoPaperRequest): LottoPaper {
//        val paperCount = calculatePaperCount(lottoPaperRequest)
//        val issuedLottoes = createIssuedLottoes(lottoNumberGenerator.generate(paperCount), IssueStatus.AUTO)
//        return LottoPaper(issuedLottoes)
//    }
//
//    private fun calculatePaperCount(lottoPaperRequest: LottoPaperRequest): Int {
//        require(lottoPaperRequest.isDivide(UNIT)) {
//            "금액은 ${UNIT}원 단위여야 합니다."
//        }
//        return lottoPaperRequest.divide(UNIT)
//    }

    private fun createIssuedLottoes(
        lottoNumbers: LottoNumbers,
        issueStatus: IssueStatus,
    ): List<IssuedLotto> {
        return getLottoEntities(lottoNumbers).map { IssuedLotto(issueStatus, it) }.toList()
    }

    private fun getLottoEntities(lottoNumbers: LottoNumbers): List<Lotto> {
        return lottoRepository.findLottoByNumbersIn(lottoNumbers.toStringWithComma())
    }
}
