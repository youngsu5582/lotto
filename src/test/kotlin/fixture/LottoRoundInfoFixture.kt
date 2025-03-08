package fixture

import lotto.domain.entity.LottoRoundInfo
import lotto.domain.entity.LottoStatus
import java.time.LocalDateTime

object LottoRoundInfoFixture {
    fun 매우_긴_진행중인_회차_생성(time: LocalDateTime = LocalDateTime.now()): LottoRoundInfo {
        return LottoRoundInfo(
            round = 10,
            startDate = time.minusYears(1),
            endDate = time.plusYears(1),
            drawDate = time.plusYears(2),
            paymentDeadline = time.plusYears(3),
            status = LottoStatus.ONGOING
        )
    }
}
