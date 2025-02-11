package lotto.controller

import auth.domain.vo.AuthenticatedMember
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
        authenticatedMember: AuthenticatedMember
    ): ApiResponse<LottoPurchaseHttpResponse> {
        val lottoPurchaseData =
            lottoPurchaseService.purchase(
                lottoPurchaseHttpRequest.toPurchaseRequest(),
                lottoPurchaseHttpRequest.lottoPublishId,
                authenticatedMember
            )
        return ApiResponse.ok(
            data = LottoPurchaseHttpResponse.from(lottoPurchaseData)
        )
    }

    @Post("/api/cancel")
    fun cancel(
        @Body lottoCancelHttpRequest: LottoCancelHttpRequest,
        authenticatedMember: AuthenticatedMember
    ): ApiResponse<LottoPurchaseHttpResponse> {
        val lottoPurchaseData =
            lottoPurchaseService.cancel(
                lottoCancelHttpRequest.billId,
                authenticatedMember
            )
        return ApiResponse.ok(
            data = LottoPurchaseHttpResponse.from(lottoPurchaseData)
        )
    }

    @Post("/api/orders")
    fun saveOrder(
        @Body lottoNumberHttpRequest: LottoNumberHttpRequest
    ): ApiResponse<LottoOrderDataHttpResponse> {
        val lottoOrderData = lottoOrderService.saveLottoOrder(lottoNumberHttpRequest.toLottoNumbers())
        return ApiResponse.ok(
            data = LottoOrderDataHttpResponse.from(lottoOrderData)
        )
    }
}
