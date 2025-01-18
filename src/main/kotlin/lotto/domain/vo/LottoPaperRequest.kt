package lotto.domain.vo

import java.math.BigDecimal

data class LottoPaperRequest(
    val amount: BigDecimal,
) {
    fun isDivide(unit: BigDecimal): Boolean {
        return amount.remainder(unit) == BigDecimal.ZERO
    }

    fun divide(unit: BigDecimal): Int {
        return amount.divide(unit).toInt()
    }
}
