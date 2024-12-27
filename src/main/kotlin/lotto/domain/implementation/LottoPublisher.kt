package lotto.domain.implementation

import common.business.Implementation
import common.business.Transaction
import common.business.Write
import lotto.domain.entity.LottoPublish
import lotto.domain.entity.LottoRoundInfo
import lotto.domain.repository.LottoPublishRepository
import lotto.domain.repository.LottoRoundInfoRepository
import lotto.domain.vo.LottoPaper
import java.time.Clock
import java.time.LocalDateTime

@Implementation
class LottoPublisher(
    private val lottoRoundInfoRepository: LottoRoundInfoRepository,
    private val lottoPublishRepository: LottoPublishRepository,
    private val clock: Clock,
) {
    @Transaction
    @Write
    fun publish(lottoPaper: LottoPaper): LottoPublish {
        val issuedAt = LocalDateTime.now(clock)
        val lottoInfo = getLottoInfo(issuedAt)
        val lottoPublish =
            lottoPublishRepository.save(
                LottoPublish(
                    lottoRoundInfo = lottoInfo,
                    issuedAt = issuedAt,
                    lottoes = lottoPaper.getLottoes(),
                    issuedLottoesStatus = lottoPaper.getIssuedStatues(),
                ),
            )
        return lottoPublish
    }

    private fun getLottoInfo(issuedAt: LocalDateTime): LottoRoundInfo {
        lottoRoundInfoRepository.findAll().forEach { println(it) }
        return lottoRoundInfoRepository.findTopByIssueDateLessThanEqualAndDrawDateGreaterThanEqual(issuedAt)
            ?.also {
                if (it.isNotOngoing() || it.isAfter(issuedAt)) {
                    throw IllegalArgumentException("현재, 모집중인 회차가 아닙니다.")
                }
            } ?: throw NoSuchElementException("$issuedAt 에 맞는 회차가 없습니다.")
    }
}
