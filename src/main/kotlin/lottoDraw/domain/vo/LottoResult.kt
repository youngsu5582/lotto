package lottoDraw.domain.vo

data class LottoResult(
    val round: Int,
    val matchCount: Int,
    val bonusMatch: Boolean
) {
    init {
        require((matchCount == 6 && bonusMatch).not()) {
            "6개의 번호와 보너스 번호가 같이 일치할 수 없습니다. 회차번호 : $round 일치한 개수 : $matchCount 보너스 여부 : $bonusMatch"
        }
    }
}
