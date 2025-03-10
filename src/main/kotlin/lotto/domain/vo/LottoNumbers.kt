package lotto.domain.vo

data class LottoNumbers(
    val numbers: List<LottoNumber>
) {
    fun toBytes() = numbers.map { it.toByte() }
}
