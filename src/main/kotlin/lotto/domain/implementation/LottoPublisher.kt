package lotto.domain.implementation

import common.business.Implementation
import common.business.Transaction
import common.business.Write
import lotto.domain.entity.LottoPublish
import lotto.domain.entity.LottoRoundInfo
import lotto.domain.repository.LottoPublishRepository
import lotto.domain.repository.LottoRoundInfoRepository
import lotto.domain.vo.LottoPaper
import java.time.LocalDateTime

@Implementation
class LottoPublisher(
    private val lottoRoundInfoRepository: LottoRoundInfoRepository,
    private val lottoPublishRepository: LottoPublishRepository,
) {
    @Transaction
    @Write
    fun publish(issuedAt: LocalDateTime, lottoPaper: LottoPaper): LottoPublish {
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

    @Transaction
    @Write
    fun unPublish(lottoPublish: LottoPublish): LottoPublish {
        if(lottoPublish.getLottoRoundInfo().isNotOngoing()){
            throw IllegalArgumentException("")
        }
        lottoPublish.cancel()
        return lottoPublish
    }

    private fun getLottoInfo(issuedAt: LocalDateTime): LottoRoundInfo {
        return lottoRoundInfoRepository.findTopByIssueDateLessThanEqualAndDrawDateGreaterThanEqual(issuedAt)
            ?.also {
                if (it.isNotOngoing() || it.isAfter(issuedAt)) {
                    throw IllegalArgumentException("현재, 모집중인 회차가 아닙니다.")
                }
            } ?: throw NoSuchElementException("$issuedAt 에 맞는 회차가 없습니다.")
    }
}
