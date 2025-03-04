package fixture

import lotto.domain.entity.LottoBill
import java.util.concurrent.atomic.AtomicInteger

object LottoBillFixture {
    private val INCREMENT = AtomicInteger(0)
    fun 무작위_멤버_영수증_저장(
        purchaseId: String,
        lottoPublishId: Long = 0,
        memberId: String = "",
        lottoRoundInfoId: Long
    ): LottoBill {
        val next = INCREMENT.getAndIncrement()
        return LottoBill(
            purchaseId = purchaseId,
            lottoPublishId = lottoPublishId + next,
            memberId = memberId + next,
            lottoRoundInfoId = lottoRoundInfoId
        )
    }
}
