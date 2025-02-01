package order.domain.vo

import java.math.BigDecimal

data class OrderRequest(
    val amount: BigDecimal
) {
}
