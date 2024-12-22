package lotto.domain.vo

data class LottoNumbers(
    private val lottoNumbers: List<List<Int>>
) {

    fun size(): Int {
        return lottoNumbers.size
    }

    fun toStringWithComma(): List<String> {
        return lottoNumbers.stream().map { it.joinToString(separator = ",") }.toList()
    }
}
