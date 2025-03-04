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

    @Read
    fun findPurchase(purchaseId: String): Purchase = purchaseRepository.findById(purchaseId.toUUID())
        .orElseThrow { NoSuchElementException("결제가 존재하지 않습니다") }

    @Read
    fun findPurchase(purchaseIds: Iterable<String>): List<Purchase> =
        purchaseRepository.findAllById(purchaseIds.map { it.toUUID() }).toList()

    private fun String.toUUID() = UUID.fromString(this)
}
