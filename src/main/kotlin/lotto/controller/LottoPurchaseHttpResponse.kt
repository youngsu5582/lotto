package lotto.controller

import lotto.service.dto.LottoPurchaseData
import lotto.service.dto.PurchaseData
import java.math.BigDecimal
import java.util.*

data class LottoPurchaseHttpResponse(
    val purchaseResponse: PurchaseResponse,
) {
    companion object {
        fun from(lottoPurchaseData: LottoPurchaseData): LottoPurchaseHttpResponse {
            return LottoPurchaseHttpResponse(
                purchaseResponse = PurchaseResponse.from(lottoPurchaseData.purchase),
            )
        }
    }
}

data class PurchaseResponse(
    val id: UUID,
    val amount: BigDecimal
) {
    companion object {
        fun from(purchaseData: PurchaseData): PurchaseResponse {
            return PurchaseResponse(
                id = purchaseData.id,
                amount = purchaseData.amount,
            )
        }
    }
}
