package lotto

import lotto.domain.lotto.LottoRoundInfo
import lotto.domain.lotto.LottoStatus
import java.time.LocalDateTime

class Fixture {
    object LottoInfoFixture {
        fun create(
            round: Long = 1,
            startDate: LocalDateTime = LocalDateTime.now(),
            endDate: LocalDateTime = LocalDateTime.now().plusDays(6),
            drawDate: LocalDateTime = LocalDateTime.now().plusDays(6).plusMinutes(30),
            paymentDeadline: LocalDateTime = LocalDateTime.now().plusYears(1),
            status: LottoStatus = LottoStatus.ONGOING
        ): LottoRoundInfo {
            return LottoRoundInfo(
                round = round,
                startDate = startDate,
                endDate = endDate,
                drawDate = drawDate,
                paymentDeadline = paymentDeadline,
                status = status
            )
        }
    }
}
