package lotto.service.dto

import lotto.domain.entity.Lotto
import lotto.domain.entity.LottoBill
import lotto.domain.entity.LottoPublish
import purchase.domain.entity.Purchase
import java.math.BigDecimal
import java.util.*

class LottoPurchaseData(
    val purchase: PurchaseData,
    val lottoPublish: LottoPublishData
) {
    companion object {
        fun from(lottoBill: LottoBill): LottoPurchaseData {
            return LottoPurchaseData(
                purchase = PurchaseData.from(lottoBill.getPurchase()),
                lottoPublish = LottoPublishData.from(lottoBill.getLottoPublish()),
            )
        }
    }
}

data class PurchaseData(
    val id: UUID,
    val amount: BigDecimal
) {
    companion object {
        fun from(purchase: Purchase): PurchaseData {
            return PurchaseData(
                id = purchase.getId(),
                amount = purchase.getTotalAmount(),
            )
        }
    }
}

data class LottoPublishData(
    val id: Long,
    val isCanceled: Boolean
) {
    companion object {
        fun from(lottoPublish: LottoPublish): LottoPublishData {
            return LottoPublishData(
                id = lottoPublish.getId(),
                isCanceled = lottoPublish.getCanceled()
            )
        }
    }
}
