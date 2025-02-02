package lotto.domain.vo

import lotto.domain.entity.IssuedLotto
import java.math.BigDecimal

data class LottoPaper(
    private val lottoes: List<IssuedLotto>,
    private val amount: BigDecimal
) {
    companion object {
        const val MAX_PURCHASE_LIMIT = 5
    }

    init {
        require(lottoes.size <= MAX_PURCHASE_LIMIT) {
            "한 번에 $MAX_PURCHASE_LIMIT 까지 구매가 가능합니다."
        }
    }

    fun getLottoes(): List<IssuedLotto> {
        return lottoes
    }

    fun getAmount(): BigDecimal = amount
}
