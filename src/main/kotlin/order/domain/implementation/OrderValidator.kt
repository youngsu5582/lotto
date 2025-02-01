package order.domain.implementation

import common.business.Implementation
import common.business.Read
import common.business.Transaction
import order.domain.repository.OrderRepository
import purchase.domain.PurchaseException
import purchase.domain.PurchaseExceptionCode
import java.math.BigDecimal

@Implementation
class OrderValidator(
    private val orderRepository: OrderRepository,
) {
    @Transaction
    @Read
    fun checkOrderValid(request: OrderDataRequest) {
        val orderData = orderRepository.findByOrderId(request.orderId).orElseThrow {
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
