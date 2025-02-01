package lotto.controller

import common.dto.ApiResponse
import common.web.Body
import common.web.HttpController
import common.web.Post
import lotto.service.LottoOrderService
import lotto.service.LottoPurchaseService

@HttpController
class LottoPurchaseController(
    private val lottoPurchaseService: LottoPurchaseService,
    private val lottoOrderService: LottoOrderService
) {
    @Post("/api/tickets")
    fun purchase(
        @Body lottoPurchaseHttpRequest: LottoPurchaseHttpRequest,
    ): ApiResponse<LottoPurchaseHttpResponse> {
        val lottoPurchaseData =
            lottoPurchaseService.purchase(
                lottoPurchaseHttpRequest.toPurchaseRequest(),
                lottoPurchaseHttpRequest.lottoPublishId
            )
        return ApiResponse.ok(
            data = LottoPurchaseHttpResponse.from(lottoPurchaseData)
        )
    }

    @Post("/api/cancel")
    fun cancel(
        @Body lottoCancelHttpRequest: LottoCancelHttpRequest
    ): ApiResponse<LottoPurchaseHttpResponse> {
        val lottoPurchaseData =
            lottoPurchaseService.cancel(
                lottoCancelHttpRequest.billId
            )
        return ApiResponse.ok(
            data = LottoPurchaseHttpResponse.from(lottoPurchaseData)
        )
    }

    @Post("/api/order")
    fun saveOrder(
        @Body lottoRequest: LottoRequest
    ): ApiResponse<LottoOrderDataHttpResponse> {
        val lottoOrderData = lottoOrderService.saveLottoOrder(lottoRequest.toLottoNumbers())
        return ApiResponse.ok(
            data = LottoOrderDataHttpResponse.from(lottoOrderData)
        )
    }
}
