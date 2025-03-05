package lotto.domain.vo

import lotto.domain.entity.LottoBill

data class LottoBills(
    val bills: List<LottoBill>
) {
    fun publishCount() = bills.map { it.getLottoPublishId() }.count()
    fun memberCount() = bills.groupBy { it.getMemberId() }.count()
}
