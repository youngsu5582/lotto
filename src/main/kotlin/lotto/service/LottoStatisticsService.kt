package lotto.service

import common.business.BusinessService
import common.business.Read
import common.business.Transaction
import common.business.Write
import lotto.domain.entity.LottoRoundInfo
import lotto.domain.entity.LottoStatistics
import lotto.domain.repository.LottoBillRepository
import lotto.domain.repository.LottoRoundInfoRepository
import lotto.domain.repository.LottoStatisticsRepository
import lotto.domain.vo.LottoBills
import lotto.service.dto.LottoStatisticsData
import purchase.domain.implementation.PurchaseReader
import purchase.domain.vo.Purchases
import java.time.LocalDateTime

@BusinessService
class LottoStatisticsService(
    private val lottoBillRepository: LottoBillRepository,
    private val lottoRoundInfoRepository: LottoRoundInfoRepository,
    private val purchaseReader: PurchaseReader,
    private val lottoStatisticsRepository: LottoStatisticsRepository
) {
    @Transaction
    @Write
    fun updateStaticInfoWithCurrentLottoRoundInfo(
        lottoRoundInfo: LottoRoundInfo,
        time: LocalDateTime
    ): LottoStatisticsData {
        val lottoBills = LottoBills(lottoBillRepository.findAllByLottoRoundInfoId(lottoRoundInfo.id!!))
        val purchases = Purchases(
            purchaseReader.findPurchase(lottoBillRepository.findPurchaseIdsByLottoRoundInfo(lottoRoundInfo.id!!))
        )

        val statistics = lottoStatisticsRepository.save(
            LottoStatistics(
                lottoRoundInfoId = lottoRoundInfo.id!!,
                memberCount = lottoBills.memberCount(),
                lottoPublishCount = lottoBills.publishCount(),
                totalPurchaseMoney = purchases.sumOfAmount(),
                updatedAt = time
            )
        )
        return LottoStatisticsData.from(statistics, lottoRoundInfo.round)
    }

    @Read
    fun getStaticInfoWithCurrentLottoRoundInfo(time: LocalDateTime): LottoStatisticsData {
        return getLottoRoundInfo(time).let {
            lottoStatisticsRepository.findByLottoRoundInfoId(it.id).let { lottoStatistics ->
                if (lottoStatistics != null) {
                    return LottoStatisticsData.from(lottoStatistics, it.round)
                }
                throw IllegalArgumentException("$time 에 해당하는 통계정보가 없습니다 ( 회차 정보 : ${it.id} )")
            }
        }
    }

    private fun getLottoRoundInfo(time: LocalDateTime) =
        lottoRoundInfoRepository.findTopByIssueDateLessThanEqualAndDrawDateGreaterThanEqual(time)
            ?: throw IllegalArgumentException("$time 에 맞는 회치가 없습니다.")
}
