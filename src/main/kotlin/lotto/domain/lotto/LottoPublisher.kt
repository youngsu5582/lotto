package lotto.domain.lotto

import common.business.Implementation
import common.business.Transaction
import common.business.Write
import lotto.domain.lotto.repository.LottoPublishRepository
import lotto.domain.lotto.repository.LottoRepository
import lotto.domain.lotto.repository.LottoRoundInfoRepository
import lotto.domain.lotto.vo.LottoPaper
import java.time.Clock
import java.time.LocalDateTime

@Implementation
class LottoPublisher(
    private val lottoRepository: LottoRepository,
    private val lottoRoundInfoRepository: LottoRoundInfoRepository,
    private val lottoPublishRepository: LottoPublishRepository,
    private val clock: Clock
) {
    @Transaction
    @Write
    fun publish(lottoPaper: LottoPaper): LottoPublish {
        val issuedAt = LocalDateTime.now(clock)
        val lottoInfo = getLottoInfo(issuedAt)
        val lottoes = getLottoEntities(lottoPaper)
        val lottoPublish = lottoPublishRepository.save(
            LottoPublish(
                lottoRoundInfo = lottoInfo,
                issuedAt = issuedAt,
                lottoes = lottoes,
                issuedLottoesStatus = lottoPaper.getIssuedStatues()
            )
        )
        return lottoPublish
    }

    private fun getLottoInfo(issuedAt: LocalDateTime): LottoRoundInfo {
        lottoRoundInfoRepository.findAll().forEach { println(it)}
        return lottoRoundInfoRepository.findTopByIssueDateLessThanEqualAndDrawDateGreaterThanEqual(issuedAt)
            ?.also {
                if (it.isNotOngoing() || it.isAfter(issuedAt)) {
                    throw IllegalArgumentException("현재, 모집중인 회차가 아닙니다.")
                }
            } ?: throw NoSuchElementException("$issuedAt 에 맞는 회차가 없습니다.")
    }

    private fun getLottoEntities(lottoPaper: LottoPaper): List<Lotto> {
        return lottoRepository.findLottoByNumbersIn(lottoPaper.toStringListWithComma())
    }
}
