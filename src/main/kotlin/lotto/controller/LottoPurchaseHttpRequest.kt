package lotto.controller

import lotto.domain.lotto.vo.Currency
import lotto.domain.lotto.vo.LottoNumbers
import lotto.domain.lotto.vo.PurchaseRequest
import lotto.domain.lotto.vo.PurchaseType
import java.math.BigDecimal

data class LottoPurchaseHttpRequest(
    private val purchaseHttpRequest: PurchaseHttpRequest,
    private val lottoRequest: LottoRequest
) {
    fun toLottoNumbers(): LottoNumbers {
        return LottoNumbers(lottoRequest.numbers)
    }

    fun toPurchaseRequest(): PurchaseRequest {
        return PurchaseRequest(
            purchaseType = purchaseHttpRequest.purchaseType,
            amount = purchaseHttpRequest.amount,
            currency = purchaseHttpRequest.currency,
            paymentKey = purchaseHttpRequest.paymentKey,
            orderId = purchaseHttpRequest.orderId,
        )
    }
}

data class LottoRequest(
    val numbers: List<List<Int>>
)

data class PurchaseHttpRequest(
    val purchaseType: PurchaseType,
    val currency: Currency,
    val amount: BigDecimal,
    val paymentKey: String,
    val orderId: String
)
