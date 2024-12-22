package lotto.domain.lotto

class IssuedLotto(
    private val issueStatus: IssueStatus,
    private val lotto: Lotto,
) {
    fun toStringWithComma(): String {
        return lotto.toStringWithComma()
    }

    fun toIntList(): List<Int> {
        return lotto.toIntList()
    }

    fun getIssueStatus(): IssueStatus = issueStatus
}

enum class IssueStatus {
    AUTO,
    MANUAL
}
