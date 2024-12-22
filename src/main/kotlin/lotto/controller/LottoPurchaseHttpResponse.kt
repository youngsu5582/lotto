package lotto.controller

import lotto.domain.lotto.LottoBill

data class LottoPurchaseHttpResponse(
    private val lottoResponse: lotto.controller.LottoResponse
) {
    companion object {
        fun from(lottoBill: LottoBill): lotto.controller.LottoPurchaseHttpResponse {
            return lotto.controller.LottoPurchaseHttpResponse(
                lottoResponse = lotto.controller.LottoResponse(listOf(listOf(1)))
            )
        }
    }
}

data class LottoResponse(
    val numbers: List<List<Int>>
)
