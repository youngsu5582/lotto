package lotto.controller

import common.dto.apiResponse
import common.web.Get
import common.web.HttpController
import lotto.service.LottoRoundInfoService
import lotto.service.LottoStatisticsService
import java.time.Clock
import java.time.LocalDateTime

@HttpController
class LottoRoundInfoController(
    private val lottoRoundInfoService: LottoRoundInfoService,
    private val lottoStatisticsService: LottoStatisticsService,
    private val clock: Clock
) {
    @Get("/api/lottoes/round-info")
    fun lottoesRoundInfo() = LocalDateTime.now(clock).let {
        apiResponse {
            status = 200
            message = "로또 회차 정보 조회"
            data = lottoRoundInfoService.getCurrentLottoRoundInfo(it).let { data -> LottoRoundInfoResponse.from(data) }
        }
    }

    @Get("/api/lottoes/statistics")
    fun totalCount() = LocalDateTime.now(clock).let {
        apiResponse {
            status = 200
            message = "로또 회차 통계 정보 조회"
            data =
                lottoStatisticsService.getStaticInfoWithCurrentLottoRoundInfo(it)
                    .let { data -> LottoStaticHttpResponse.from(data) }
        }
    }
}
