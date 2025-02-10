package lotto.domain.implementation

import common.business.Implementation
import common.business.Read
import lotto.domain.entity.LottoBill
import lotto.domain.repository.LottoBillRepository

@Implementation
class LottoReader(
    private val lottoBillRepository: LottoBillRepository
) {
    @Read
    fun findBill(billId: Long, memberId: String): LottoBill =
        lottoBillRepository.findById(billId)
            .map { bill ->
                bill.takeIf { it.isOwner(memberId) }
                    ?: throw IllegalArgumentException("$memberId is Not Owner")
            }
            .orElseThrow { IllegalArgumentException("Not Exist Bill") }
}
