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
    fun findBill(billId: Long): LottoBill {
        return lottoBillRepository
            .findById(billId)
            .orElseThrow { IllegalArgumentException("Not Exist Bill") }
    }
}
