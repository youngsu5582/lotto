package lotto.domain

import lotto.domain.lotto.LottoRoundInfo
import lotto.domain.lotto.LottoStatus
import org.assertj.core.api.Assertions.assertThat
import java.time.LocalDateTime
import kotlin.test.Test

class LottoRoundInfoTest {
    @Test
    fun `구매 가능시간 보다 지나면 참을 반환한다`() {
        val ldt = LocalDateTime.now()
        val edt = ldt.plusDays(1)
        val lottoRoundInfo = LottoRoundInfo(
            round = 1,
            startDate = ldt,
            endDate = edt,
            drawDate = ldt.plusDays(2),
            paymentDeadline = ldt.plusYears(1),
            status = LottoStatus.ONGOING
        )
        assertThat(lottoRoundInfo.isAfter(edt.plusNanos(1))).isTrue()
    }
}
