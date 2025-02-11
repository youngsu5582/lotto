package lotto.domain.implementation

import common.business.Implementation
import common.business.Transaction
import common.business.Write
import lotto.domain.entity.LottoBill
import lotto.domain.entity.LottoPublish
import lotto.domain.repository.LottoBillRepository
import purchase.domain.entity.Purchase

@Implementation
class LottoWriter(
    private val lottoBillRepository: LottoBillRepository
) {

    @Transaction
    @Write
    fun saveBill(purchase: Purchase, lottoPublish: LottoPublish, memberId: String): LottoBill {
        return lottoBillRepository.save(
            LottoBill(
                purchase = purchase,
                lottoPublish = lottoPublish,
                memberId = memberId
            )
        )
    }
}
