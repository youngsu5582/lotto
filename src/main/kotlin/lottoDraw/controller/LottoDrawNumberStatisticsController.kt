package lottoDraw.controller

import common.dto.ApiResponse
import common.dto.apiResponse
import common.web.Get
import common.web.HttpController
import lotto.domain.vo.LottoNumber
import lotto.domain.vo.LottoNumbers
import lottoDraw.service.LottoDrawNumberStatisticsService
import org.springframework.web.bind.annotation.RequestParam

@HttpController
class LottoDrawNumberStatisticsController(
    private val lottoDrawNumberStatisticsService: LottoDrawNumberStatisticsService
) {

    @Get("/api/draw/statistics/numbers")
    fun getNumberStatistics(@RequestParam("numbers") numbers: List<Int>): ApiResponse<LottoDrawPreviewNumberHttpResponse> {
        val lottoNumbers = LottoNumbers(numbers.sorted().map { LottoNumber(it.toByte()) })
        val lottoNumberCountsData = lottoDrawNumberStatisticsService.getLottoNumberCounts(lottoNumbers)
        val lottoDrawResultData = lottoDrawNumberStatisticsService.getLottoDraws(lottoNumbers)

        return apiResponse {
            status = 200
            message = "로또 숫자 당첨 통계 정보 조회"
            data = LottoDrawPreviewNumberHttpResponse(
                lottoNumberCountResponse = LottoNumberCountHttpResponse.from(lottoNumberCountsData),
                lottoResultResponse = LottoDrawResultHttpResponse.from(lottoDrawResultData)
            )
        }
    }
}
