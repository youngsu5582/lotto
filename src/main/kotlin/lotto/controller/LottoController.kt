package lotto.controller

import common.dto.ApiResponse
import common.web.Body
import common.web.HttpController
import common.web.Post
import lotto.service.LottoPurchaseService

@HttpController
class LottoController(
    private val lottoPurchaseService: LottoPurchaseService,
) {
    @Post("/api/tickets")
    fun purchase(
        @Body lottoPurchaseHttpRequest: LottoPurchaseHttpRequest,
    ): ApiResponse<LottoPurchaseHttpResponse> {
        val lottoPurchaseData =
            lottoPurchaseService.purchase(
                lottoPurchaseHttpRequest.toPurchaseRequest(),
                lottoPurchaseHttpRequest.toLottoNumbers()
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
}
