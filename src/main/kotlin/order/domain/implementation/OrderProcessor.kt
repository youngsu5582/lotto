package order.domain.implementation

import common.business.Implementation
import common.business.Transaction
import common.business.Write
import order.domain.vo.OrderRequest
import order.domain.entity.Order
import order.domain.repository.OrderRepository

@Implementation
class OrderProcessor(
    private val orderRepository: OrderRepository,
    private val orderIdGenerator: OrderIdGenerator
) {
    @Transaction
    @Write
    fun saveOrder(orderRequest: OrderRequest): Order {
        val orderId = orderIdGenerator.getOrderId()
        return orderRepository.save(
            Order(
                orderId = orderId.toString(),
                amount = orderRequest.amount
            )
        )
    }
}
