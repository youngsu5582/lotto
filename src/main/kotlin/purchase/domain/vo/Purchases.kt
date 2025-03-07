package purchase.domain.vo

import purchase.domain.entity.Purchase

data class Purchases(
    val purchases: List<Purchase>
) {
    fun sumOfAmount() = purchases.map { it.getTotalAmount() }.sumOf { it }
}
