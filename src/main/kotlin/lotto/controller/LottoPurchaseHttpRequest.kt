package lotto.controller

import com.fasterxml.jackson.annotation.JsonCreator
import lotto.domain.vo.Currency
import lotto.domain.vo.LottoNumbersPack
import lotto.domain.vo.LottoPurchaseRequest
import lotto.domain.vo.PurchaseType
import java.math.BigDecimal

data class LottoPurchaseHttpRequest @JsonCreator constructor(
    val purchaseHttpRequest: PurchaseHttpRequest,
    val lottoPublishId: Long
) {
    fun toPurchaseRequest(): LottoPurchaseRequest {
        return LottoPurchaseRequest(
            purchaseType = purchaseHttpRequest.purchaseType,
            amount = purchaseHttpRequest.amount,
            currency = purchaseHttpRequest.currency,
            paymentKey = purchaseHttpRequest.paymentKey,
            orderId = purchaseHttpRequest.orderId,
        )
    }
}

data class LottoNumberHttpRequest @JsonCreator constructor(
    val numbers: List<List<Int>>
) {
    fun toLottoNumbers(): LottoNumbersPack {
        return LottoNumbersPack(numbers)
    }
}

data class PurchaseHttpRequest(
    val purchaseType: PurchaseType,
    val currency: Currency,
    val amount: BigDecimal,
    val paymentKey: String,
    val orderId: String,
)
