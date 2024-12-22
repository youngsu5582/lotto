package lotto.domain.lotto.vo

import java.util.stream.Stream

data class LottoNumbers(
    private val lottoNumbers: List<List<Int>>
) {
    fun stream(): Stream<List<Int>> {
        return lottoNumbers.stream()
    }

    fun size(): Int {
        return lottoNumbers.size
    }
}
