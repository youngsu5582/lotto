package lotto.domain.vo

import lotto.domain.Lotto

data class LottoPaper(
    private val lottoes: List<Lotto>
) {
    companion object {
        const val MAX_PURCHASE_LIMIT = 5

        fun from(lottoNumbers: List<List<Int>>): LottoPaper {
            return LottoPaper(lottoNumbers.stream().map { Lotto(it) }.toList())
        }
    }

    init {
        require(lottoes.size <= 5) {
            "한 번에 $MAX_PURCHASE_LIMIT 까지 구매가 가능합니다."
        }
    }

    fun toStringListWithComma(): List<String> {
        return lottoes.stream().map { it.toStringWithComma() }.toList()
    }
}
