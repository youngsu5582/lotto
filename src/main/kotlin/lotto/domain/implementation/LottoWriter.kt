package lotto.domain.implementation

import common.business.Implementation
import common.business.Transaction
import common.business.Write
import lotto.domain.entity.LottoBill
import lotto.domain.repository.LottoBillRepository
import java.util.*

@Implementation
class LottoWriter(
    private val lottoBillRepository: LottoBillRepository,
) {

    @Transaction
    @Write
    fun saveBill(purchaseId: UUID, lottoPublishId: Long, memberId: String, lottoRoundInfoId: Long): LottoBill {
        return lottoBillRepository.save(
            LottoBill(
                purchaseId = purchaseId.toString(),
                lottoPublishId = lottoPublishId,
                memberId = memberId,
                lottoRoundInfoId = lottoRoundInfoId
            )
        )
    }
}
