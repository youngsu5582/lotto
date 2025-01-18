package lotto.service.dto

import lotto.domain.entity.Lotto
import lotto.domain.entity.LottoBill
import org.springframework.boot.web.server.Cookie
import java.math.BigDecimal
import java.util.*

class LottoPurchaseData(
    val purchase: PurchaseData,
    val lottoPublish: LottoPublishData
) {
    companion object {
        fun from(lottoBill: LottoBill): LottoPurchaseData {
            return LottoPurchaseData(
                purchase = PurchaseData.from(lottoBill),
                lottoPublish = LottoPublishData.from(lottoBill),
            )
        }
    }
}

data class PurchaseData(
    val id: UUID,
    val amount: BigDecimal
) {
    companion object {
        fun from(lottoBill: LottoBill): PurchaseData {
            return PurchaseData(
                id = lottoBill.getPurchase().getId()!!,
                amount = lottoBill.getPurchase().getTotalAmount(),
            )
        }
    }
}

data class LottoPublishData(
    val id: Long,
    val numbers: List<Lotto>
) {
    companion object {
        fun from(lottoBill: LottoBill): LottoPublishData {
            return LottoPublishData(
                id = lottoBill.getLottoPublish().getId()!!,
                numbers = lottoBill.getLottoPublish().getLottoes(),
            )
        }
    }
}
