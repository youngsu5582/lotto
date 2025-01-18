package lotto.domain.vo

import lotto.domain.entity.IssueStatus
import lotto.domain.entity.IssuedLotto
import lotto.domain.entity.Lotto

data class LottoPaper(
    private val lottoes: List<IssuedLotto>,
) {
    companion object {
        const val MAX_PURCHASE_LIMIT = 5
    }

    init {
        require(lottoes.size <= 5) {
            "한 번에 $MAX_PURCHASE_LIMIT 까지 구매가 가능합니다."
        }
    }

    fun toIntList(): List<List<Int>> {
        return lottoes.stream().map { it.toIntList() }.toList()
    }

    fun getIssuedStatues(): List<IssueStatus> {
        return lottoes.stream().map { it.getIssueStatus() }.toList()
    }

    fun getLottoes(): List<Lotto> {
        return lottoes.stream().map { it.getLotto() }.toList()
    }
}
