package lotto.domain.lotto.vo

import lotto.domain.lotto.IssueStatus
import lotto.domain.lotto.IssuedLotto

data class LottoPaper(
    private val lottoes: List<IssuedLotto>
) {
    companion object {
        const val MAX_PURCHASE_LIMIT = 5
    }

    init {
        require(lottoes.size <= 5) {
            "한 번에 $MAX_PURCHASE_LIMIT 까지 구매가 가능합니다."
        }
    }

    fun toStringListWithComma(): List<String> {
        return lottoes.stream().map { it.toStringWithComma() }.toList()
    }

    fun toIntList(): List<List<Int>> {
        return lottoes.stream().map { it.toIntList() }.toList()
    }

    fun getIssuedStatues(): List<IssueStatus> {
        return lottoes.stream().map { it.getIssueStatus() }.toList()
    }
}
