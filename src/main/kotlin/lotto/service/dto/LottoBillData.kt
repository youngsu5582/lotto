package lotto.service.dto

import lotto.domain.entity.LottoPublish
import lotto.domain.entity.PublishedLotto
import purchase.domain.entity.Purchase
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class LottoBillData(
    val id: Long,
    val purchase: PurchaseData,
    val lottoPublish: LottoPublishData
)

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
    val status: String,
    val lottoRoundInfo: LottoRoundInfoData,
    val lottoes: List<List<Int>>,
    val issuedAt: LocalDateTime
) {
    companion object {
        fun from(lottoPublish: LottoPublish, publishedLottoes: List<PublishedLotto>): LottoPublishData {
            return LottoPublishData(
                id = lottoPublish.getId(),
                status = lottoPublish.status(),
                lottoRoundInfo = LottoRoundInfoData.from(lottoPublish.getLottoRoundInfo()),
                issuedAt = lottoPublish.getIssuedAt(),
                lottoes = publishedLottoes.map { it.getLottoes() }
            )
        }

        fun from(lottoPublish: LottoPublish): LottoPublishData {
            return LottoPublishData(
                id = lottoPublish.getId(),
                status = lottoPublish.status(),
                lottoRoundInfo = LottoRoundInfoData.from(lottoPublish.getLottoRoundInfo()),
                issuedAt = lottoPublish.getIssuedAt(),
                lottoes = listOf()
            )
        }
    }
}
