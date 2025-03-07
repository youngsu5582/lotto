package lotto.service

import common.business.BusinessService
import lotto.domain.repository.LottoRoundInfoRepository
import lotto.service.dto.LottoRoundInfoData
import java.time.LocalDateTime

@BusinessService
class LottoRoundInfoService(
    private val lottoRoundInfoRepository: LottoRoundInfoRepository,
) {
    fun getCurrentLottoRoundInfo(time: LocalDateTime): LottoRoundInfoData {
        val lottoRoundInfo = getLottoRoundInfo(time)
        return LottoRoundInfoData.from(lottoRoundInfo)
    }

    private fun getLottoRoundInfo(time: LocalDateTime) =
        lottoRoundInfoRepository.findTopByIssueDateLessThanEqualAndDrawDateGreaterThanEqual(time)
            ?: throw IllegalArgumentException("$time 에 맞는 회치가 없습니다.")
}
