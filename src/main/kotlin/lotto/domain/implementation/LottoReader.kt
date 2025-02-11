package lotto.domain.implementation

import common.business.Implementation
import common.business.Read
import lotto.domain.entity.LottoBill
import lotto.domain.repository.LottoBillRepository
import lotto.domain.repository.PublishedLottoRepository

@Implementation
class LottoReader(
    private val lottoBillRepository: LottoBillRepository,
    private val publishedLottoRepository: PublishedLottoRepository
) {
    @Read
    fun findBill(billId: Long, memberId: String): LottoBill =
        lottoBillRepository.findById(billId)
            .map { bill ->
                bill.takeIf { it.isOwner(memberId) }
                    ?: throw IllegalArgumentException("$memberId is Not Owner")
            }
            .orElseThrow { IllegalArgumentException("Not Exist Bill") }

    @Read
    fun findBill(memberId: String) = lottoBillRepository.findByMemberId(memberId)

    @Read
    fun findPublishedLotto(publishId: Long) = publishedLottoRepository.findByLottoPublishId(publishId)
}
