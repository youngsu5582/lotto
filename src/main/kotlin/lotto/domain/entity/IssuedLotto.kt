package lotto.domain.entity

data class IssuedLotto(
    val issueStatus: IssueStatus,
    val lotto: Lotto,
) {
}

enum class IssueStatus {
    AUTO,
    MANUAL,
}
