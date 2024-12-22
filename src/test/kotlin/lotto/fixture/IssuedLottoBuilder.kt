package lotto.fixture

import lotto.domain.lotto.IssueStatus
import lotto.domain.lotto.IssuedLotto
import lotto.domain.lotto.Lotto

class IssuedLottoBuilder {
    private var issueStatus: IssueStatus = IssueStatus.AUTO
    private var numbers: List<Int> = listOf(1, 2, 3, 4, 5, 6)

    fun withIssueStatus(issueStatus: IssueStatus) = apply { this.issueStatus = issueStatus }
    fun withNumbers(numbers: List<Int>) = apply { this.numbers = numbers }
    fun build(): IssuedLotto {
        return IssuedLotto(issueStatus, Lotto(numbers))
    }
}
