package lotto.domain.implementation

import common.business.Implementation
import common.business.Read
import common.business.Transaction
import lotto.domain.entity.IssueStatus
import lotto.domain.entity.IssuedLotto
import lotto.domain.entity.Lotto
import lotto.domain.repository.LottoRepository
import lotto.domain.vo.LottoNumbersPack
import lotto.domain.vo.LottoPaper
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
        lottoNumbersPack: LottoNumbersPack,
    ): LottoPaper {
        val issuedLottoes = createIssuedLottoes(lottoNumbersPack, IssueStatus.MANUAL)
        val amount = calculateAmount(lottoNumbersPack)
        return LottoPaper(lottoes = issuedLottoes, amount = amount)
    }

    private fun calculateAmount(lottoNumbersPack: LottoNumbersPack): BigDecimal {
        return lottoNumbersPack.calculatePrice(UNIT)
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
        lottoNumbersPack: LottoNumbersPack,
        issueStatus: IssueStatus,
    ): List<IssuedLotto> {
        val lottoes = getLottoEntities(lottoNumbersPack)
        require(lottoNumbersPack.hasSize(lottoes.size)) { IllegalStateException("입력한 숫자와 조회한 숫자가 다릅니다.") }
        return lottoes.map { IssuedLotto(issueStatus, it) }.toList()
    }

    private fun getLottoEntities(lottoNumbersPack: LottoNumbersPack): List<Lotto> {
        return lottoRepository.findLottoByNumbersIn(lottoNumbersPack.toStringWithComma())
    }
}
