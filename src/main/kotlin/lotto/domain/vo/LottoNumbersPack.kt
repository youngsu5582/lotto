package lotto.domain.vo

import java.math.BigDecimal

data class LottoNumbersPack(
    private val lottoNumbers: List<List<Int>>,
) {
    fun hasSize(size: Int): Boolean {
        return lottoNumbers.size == size
    }

    fun calculatePrice(unitPrice: BigDecimal): BigDecimal {
        return unitPrice.multiply(lottoNumbers.size.toBigDecimal())
    }

    fun toStringWithComma(): List<String> {
        return lottoNumbers.stream().map { it.joinToString(separator = ",") }.toList()
    }
}
