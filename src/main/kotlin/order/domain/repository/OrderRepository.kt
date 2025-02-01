package order.domain.repository

import org.springframework.data.repository.CrudRepository
import purchase.domain.entity.Order
import java.util.*

interface OrderRepository : CrudRepository<Order, UUID> {
    fun findByOrderId(orderId: String): Optional<Order>
}
