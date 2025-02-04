package purchase.domain.implementation

import common.business.Implementation
import purchase.domain.PurchaseException
import purchase.domain.PurchaseExceptionCode
import purchase.domain.entity.Purchase
import java.math.BigDecimal

@Implementation
class PurchaseValidator {

    fun checkCancelValid(purchase: Purchase) {
        if (purchase.isCanceled()) {
            throw PurchaseException(PurchaseExceptionCode.ALREADY_CANCELED)
        }
        if (purchase.isNotSuccess()) {
            throw PurchaseException(PurchaseExceptionCode.NOT_CONFIRMED)
        }
    }
}
