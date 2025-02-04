package order.domain.vo

import purchase.domain.entity.Order
import java.math.BigDecimal

class OrderData(
    val orderId: String,
    val amount: BigDecimal
) {
    companion object {
        fun from(order: Order): OrderData {
            return OrderData(
                orderId = order.getOrderId(),
                amount = order.getAmount()
            )
        }
    }
}
