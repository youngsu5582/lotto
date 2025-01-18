package purchase.domain.implementation

import common.business.Implementation
import common.business.Read
import common.business.Transaction
import common.business.Write
import purchase.domain.PurchaseException
import purchase.domain.PurchaseExceptionCode
import purchase.domain.entity.PurchaseTemporary
import purchase.domain.repository.PurchaseTemporaryRepository
import java.math.BigDecimal

@Implementation
class OrderDataProcessor(
    private val purchaseTemporaryRepository: PurchaseTemporaryRepository,
) {
    @Transaction
    @Write
    fun saveTemporary(request: OrderDataRequest): PurchaseTemporary {
        return purchaseTemporaryRepository.save(
            PurchaseTemporary(
                orderId = request.orderId,
                amount = request.amount
            )
        )
    }

    @Transaction
    @Read
    fun checkOrderValid(request: OrderDataRequest) {
        val orderData = purchaseTemporaryRepository.findByOrderId(request.orderId).orElseThrow {
            PurchaseException(PurchaseExceptionCode.NOT_EXIST_ORDER_ID)
        }
        if (orderData.isNotEqualAmount(request.amount)) {
            throw PurchaseException(PurchaseExceptionCode.NOT_VALID_ORDER_ID)
        }
    }
}

data class OrderDataRequest(
    val orderId: String,
    val amount: BigDecimal,
)
