package lotto.domain.vo

data class LottoNumber(
    val number: Byte
) {
    init {
        require(number in 1..45) {
            "로또 번호는 1부터 45 사이어야 합니다. (입력값: $number)"
        }
    }

    fun toByte() = number
}
