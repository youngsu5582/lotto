package purchase.domain.implementation

import common.business.Implementation
import purchase.domain.PurchaseException
import purchase.domain.PurchaseExceptionCode
import purchase.domain.entity.Purchase
import purchase.domain.repository.PurchaseRepository
import java.util.*

@Implementation
class PurchaseValidator(
    private val purchaseRepository: PurchaseRepository
) {

    fun checkCancelValid(purchaseId: String): Purchase {
        val purchase = purchaseRepository.findById(UUID.fromString(purchaseId))
            .orElseThrow { IllegalArgumentException("Not Exist Purchase") }

        if (purchase.isCanceled()) {
            throw PurchaseException(PurchaseExceptionCode.ALREADY_CANCELED)
        }
        if (purchase.isNotSuccess()) {
            throw PurchaseException(PurchaseExceptionCode.NOT_CONFIRMED)
        }
        return purchase
    }
}
