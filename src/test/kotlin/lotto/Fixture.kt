package lotto

import TestConstant
import lotto.domain.entity.LottoRoundInfo
import lotto.domain.entity.LottoStatus
import java.time.LocalDateTime

class Fixture {
    object LottoInfoFixture {
        fun create(
            round: Long = 1,
            startDate: LocalDateTime = LocalDateTime.now(),
            endDate: LocalDateTime = LocalDateTime.now().plusDays(6),
            drawDate: LocalDateTime = LocalDateTime.now().plusDays(6).plusMinutes(30),
            paymentDeadline: LocalDateTime = LocalDateTime.now().plusYears(1),
            status: LottoStatus = LottoStatus.ONGOING,
        ): LottoRoundInfo {
            return LottoRoundInfo(
                round = round,
                startDate = startDate,
                endDate = endDate,
                drawDate = drawDate,
                paymentDeadline = paymentDeadline,
                status = status,
            )
        }
    }

    object LottoRoundFixture {
        fun createOngoingLottoRoundInfo(dateTime: LocalDateTime = TestConstant.DATE_TIME): LottoRoundInfo {
            return LottoRoundInfo(
                id = null,
                round = 1,
                startDate = dateTime.minusHours(1),
                endDate = dateTime.plusHours(1),
                drawDate = dateTime.plusHours(2),
                paymentDeadline = dateTime.plusYears(1)
            )
        }
    }


}
