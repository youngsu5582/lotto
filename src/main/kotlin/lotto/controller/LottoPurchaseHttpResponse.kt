package lotto.controller

import lotto.service.dto.LottoBillData
import lotto.service.dto.PurchaseData
import java.math.BigDecimal
import java.util.*

data class LottoPurchaseHttpResponse(
    val purchaseResponse: PurchaseResponse,
) {
    companion object {
        fun from(lottoBillData: LottoBillData): LottoPurchaseHttpResponse {
            return LottoPurchaseHttpResponse(
                purchaseResponse = PurchaseResponse.from(lottoBillData.purchase),
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
