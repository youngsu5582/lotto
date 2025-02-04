package lotto.controller

import lotto.service.dto.LottoOrderData

data class LottoOrderDataHttpResponse(
    val lottoPublishId: Long,
    val orderId: String
) {
    companion object {
        fun from(lottoOrderData: LottoOrderData): LottoOrderDataHttpResponse {
            return LottoOrderDataHttpResponse(
                lottoPublishId = lottoOrderData.lottoPublishId,
                orderId = lottoOrderData.orderData.orderId
            )
        }
    }
}
