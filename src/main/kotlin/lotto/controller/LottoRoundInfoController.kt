package lotto.controller

import common.dto.apiResponse
import common.web.Get
import common.web.HttpController
import lotto.service.LottoRoundInfoService

@HttpController
class LottoRoundInfoController(
    private val lottoRoundInfoService: LottoRoundInfoService
) {
    @Get("/api/lottoes/round-info")
    fun lottoesRoundInfo(
    ) = apiResponse {
        status = 200
        message = "로또 회차 정보 조회"
        data = lottoRoundInfoService.getCurrentLottoRoundInfo().let { LottoRoundInfoResponse.from(it) }
    }
}
