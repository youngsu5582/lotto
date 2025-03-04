package lotto.domain.implementation

import common.business.Implementation
import common.business.Read
import common.business.Transaction
import common.business.Write
import lotto.domain.entity.LottoPublish
import lotto.domain.entity.LottoPublishStatus
import lotto.domain.entity.LottoRoundInfo
import lotto.domain.entity.PublishedLotto
import lotto.domain.repository.LottoPublishRepository
import lotto.domain.repository.LottoRoundInfoRepository
import lotto.domain.repository.PublishedLottoRepository
import lotto.domain.vo.LottoPaper
import java.time.LocalDateTime

@Implementation
class LottoPublisher(
    private val lottoRoundInfoRepository: LottoRoundInfoRepository,
    private val lottoPublishRepository: LottoPublishRepository,
    private val publishedLottoRepository: PublishedLottoRepository,
) {
    @Transaction
    @Write
    fun complete(publishId: Long): LottoPublish {
        val lottoPublish = getLottoPublish(publishId)
        lottoPublish.complete()
        return lottoPublish
    }

    @Transaction
    @Write
    fun pending(publishId: Long): LottoPublish {
        val lottoPublish = getLottoPublish(publishId)
        lottoPublish.pending()
        return lottoPublish
    }

    @Transaction
    @Write
    fun cancel(publishId: Long): LottoPublish {
        val lottoPublish = getLottoPublish(publishId)
        lottoPublish.cancel()
        return lottoPublish
    }

    @Transaction
    @Write
    fun waiting(publishId: Long): LottoPublish {
        val lottoPublish = getLottoPublish(publishId)
        lottoPublish.waiting()
        return lottoPublish
    }

    @Transaction
    @Read
    fun read(publishId: Long): LottoPublish = lottoPublishRepository.findById(publishId).orElseThrow {
        NoSuchElementException("Not Exist LottoPublish")
    }

    @Transaction
    @Write
    fun publish(issuedAt: LocalDateTime, lottoPaper: LottoPaper): LottoPublish {
        val lottoInfo = getLottoInfo(issuedAt)
        val lottoPublish =
            lottoPublishRepository.save(
                LottoPublish(
                    lottoRoundInfo = lottoInfo,
                    issuedAt = issuedAt,
                    status = LottoPublishStatus.WAITING
                ),
            )
        publishedLottoRepository.saveAll(lottoPaper.getLottoes().map {
            PublishedLotto(
                lottoPublish = lottoPublish,
                lotto = it.lotto,
                status = it.issueStatus,
            )
        })
        return lottoPublish
    }

    @Transaction
    @Write
    fun unPublish(publishId: Long): LottoPublish {
        val lottoPublish = getLottoPublish(publishId)
        lottoPublish.cancel()
        return lottoPublish
    }

    private fun getLottoPublish(publishId: Long): LottoPublish {
        return lottoPublishRepository.findById(publishId)
            .orElseThrow { IllegalArgumentException("Not Exist Publish ( 결제 키 : $publishId )") }
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
