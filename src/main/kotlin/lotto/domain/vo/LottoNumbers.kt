package lotto.domain.vo

data class LottoNumbers(
    val value: List<LottoNumber>
) {
    fun toBytes() = value.map { it.number }

    fun check(other: LottoNumbers): Int = other.value.count { this.contains(it) }
    fun contains(number: LottoNumber) = value.contains(number)

    companion object {
        fun from(numbers: List<Byte>): LottoNumbers {
            return LottoNumbers(numbers.sorted().map { LottoNumber(it) })
        }
    }
}

