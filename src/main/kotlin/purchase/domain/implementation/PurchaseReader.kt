package purchase.domain.implementation

import common.business.Implementation
import common.business.Read
import common.business.Transaction
import purchase.domain.PurchaseException
import purchase.domain.PurchaseExceptionCode
import purchase.domain.entity.Purchase
import purchase.domain.repository.PurchaseInfoRepository
import purchase.domain.repository.PurchaseRepository
import purchase.domain.vo.PurchaseRequest
import java.util.*

@Implementation
class PurchaseReader(
    private val purchaseRepository: PurchaseRepository,
    private val purchaseInfoRepository: PurchaseInfoRepository,
) {
    @Transaction
    @Read
    fun existPurchase(purchaseRequest: PurchaseRequest) {
        purchaseRepository.findByPaymentKey(purchaseRequest.paymentKey).ifPresent {
            if (it.isSuccess()) {
                throw PurchaseException(PurchaseExceptionCode.ALREADY_PROCESS)
            }
        }
        purchaseRepository.findByOrderId(purchaseRequest.orderId).ifPresent {
            throw PurchaseException(PurchaseExceptionCode.DUPLICATE_ORDER_ID)
        }
    }

    fun findPurchase(purchaseId: UUID): Purchase = purchaseRepository.findById(purchaseId)
        .orElseThrow { NoSuchElementException("결제가 존재하지 않습니다") }
}
