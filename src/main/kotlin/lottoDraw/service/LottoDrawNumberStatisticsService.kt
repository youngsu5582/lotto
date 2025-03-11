package lottoDraw.service

import common.business.BusinessService
import common.business.Read
import common.business.Transaction
import lotto.domain.vo.LottoNumbers
import lottoDraw.domain.repository.LottoDrawRepository
import lottoDraw.domain.repository.LottoNumberCountRepository
import lottoDraw.domain.vo.LottoDrawResultData
import lottoDraw.domain.vo.LottoDraws
import lottoDraw.service.dto.LottoNumberCountData

@BusinessService
class LottoDrawNumberStatisticsService(
    private val lottoNumberCountRepository: LottoNumberCountRepository,
    private val lottoDrawRepository: LottoDrawRepository
) {
    @Transaction
    @Read
    fun getLottoNumberCounts(lottoNumbers: LottoNumbers): List<LottoNumberCountData> {
        val lottoNumberCount = lottoNumberCountRepository.findByNumberIn(lottoNumbers.toBytes())
        return lottoNumberCount.map { LottoNumberCountData.from(it) }
    }

    @Transaction
    @Read
    fun getLottoDraws(lottoNumbers: LottoNumbers): List<LottoDrawResultData> {
        val lottoDraws = LottoDraws(
            lottoDrawRepository.findAllWithFiveOrMoreMatches(lottoNumbers.toBytes()).map { it.toLottoDraw() }
        )
        return lottoDraws.matchResult(lottoNumbers)
    }
}
