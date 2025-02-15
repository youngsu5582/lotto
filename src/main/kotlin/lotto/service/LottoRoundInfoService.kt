package lotto.service

import common.web.HttpController
import lotto.domain.repository.LottoRoundInfoRepository
import lotto.service.dto.LottoRoundInfoData
import java.time.Clock
import java.time.LocalDateTime

@HttpController
class LottoRoundInfoService(
    private val clock: Clock,
    private val lottoRoundInfoRepository: LottoRoundInfoRepository
) {
    fun getCurrentLottoRoundInfo(): LottoRoundInfoData {
        val time = LocalDateTime.now(clock)
        val lottoRoundInfo = lottoRoundInfoRepository.findTopByIssueDateLessThanEqualAndDrawDateGreaterThanEqual(time)
            ?: throw IllegalArgumentException("$time 에 맞는 회치가 없습니다.")
        return LottoRoundInfoData.from(lottoRoundInfo)
    }
}
