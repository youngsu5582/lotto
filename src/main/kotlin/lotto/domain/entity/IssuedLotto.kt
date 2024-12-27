package lotto.domain.entity

class IssuedLotto(
    private val issueStatus: IssueStatus,
    private val lotto: Lotto,
) {
    fun toIntList(): List<Int> {
        return lotto.toIntList()
    }

    fun getIssueStatus(): IssueStatus = issueStatus

    fun getLotto(): Lotto = lotto
}

enum class IssueStatus {
    AUTO,
    MANUAL,
}
