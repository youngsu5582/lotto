package lottoDraw.controller

import common.dto.ApiResponse
import common.dto.apiResponse
import common.web.Body
import common.web.Get
import common.web.HttpController
import lotto.domain.vo.LottoNumber
import lotto.domain.vo.LottoNumbers
import lottoDraw.service.LottoDrawNumberStatisticsService

@HttpController
class LottoDrawNumberStatisticsController(
    private val lottoDrawNumberStatisticsService: LottoDrawNumberStatisticsService
) {

    @Get("/api/draw/statistics/numbers")
    fun getNumberStatistics(@Body lottoDrawPreviewNumberHttpRequest: LottoDrawPreviewNumberHttpRequest): ApiResponse<LottoDrawPreviewNumberHttpResponse> {
        val lottoNumbers = LottoNumbers(
            lottoDrawPreviewNumberHttpRequest.numbers.map { LottoNumber(it) }
        )
        val lottoNumberCountsData = lottoDrawNumberStatisticsService.getLottoNumberCounts(lottoNumbers)
        val lottoResultsData = lottoDrawNumberStatisticsService.getLottoDraws(lottoNumbers)

        return apiResponse {
            status = 200
            message = "로또 숫자 당첨 통계 정보 조회"
            data = LottoDrawPreviewNumberHttpResponse(
                lottoNumberCountResponse = LottoNumberCountHttpResponse.from(lottoNumberCountsData),
                lottoResultResponse = LottoResultHttpResponse.from(lottoResultsData)
            )
        }
    }
}
