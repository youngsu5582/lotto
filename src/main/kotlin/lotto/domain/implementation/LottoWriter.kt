package lotto.domain.implementation

import common.business.Implementation
import common.business.Transaction
import common.business.Write
import lotto.domain.entity.LottoBill
import lotto.domain.repository.LottoBillRepository
import purchase.domain.implementation.PurchaseReader
import java.util.*

@Implementation
class LottoWriter(
    private val lottoBillRepository: LottoBillRepository,
    private val lottoPublisher: LottoPublisher,
    private val purchaseReader: PurchaseReader
) {

    @Transaction
    @Write
    fun saveBill(purchaseId: UUID, lottoPublishId: Long, memberId: String): LottoBill {
        val purchase = purchaseReader.findPurchase(purchaseId)
        val lottoPublish = lottoPublisher.findPublish(publishId = lottoPublishId)
        return lottoBillRepository.save(
            LottoBill(
                purchase = purchase,
                lottoPublish = lottoPublish,
                memberId = memberId
            )
        )
    }
}
