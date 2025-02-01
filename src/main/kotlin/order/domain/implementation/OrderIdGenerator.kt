package order.domain.implementation

import common.business.Implementation
import java.util.*

@Implementation
class OrderIdGenerator {
    fun getOrderId(): UUID {
        return UUID.randomUUID()
    }
}
