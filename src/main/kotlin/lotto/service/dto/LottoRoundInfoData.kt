package lotto.service.dto

import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import lotto.domain.entity.LottoRoundInfo
import lotto.domain.entity.LottoStatus
import java.time.LocalDateTime

class LottoRoundInfoData(
    val round: Long,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val drawDate: LocalDateTime,
    val paymentDeadline: LocalDateTime,
    @Enumerated(EnumType.STRING)
    val status: LottoStatus = LottoStatus.ONGOING,
) {
    companion object {
        fun from(lottoRoundInfo: LottoRoundInfo): LottoRoundInfoData {
            return LottoRoundInfoData(
                startDate = lottoRoundInfo.startDate,
                endDate = lottoRoundInfo.endDate,
                drawDate = lottoRoundInfo.drawDate,
                status = lottoRoundInfo.status,
                paymentDeadline = lottoRoundInfo.paymentDeadline,
                round = lottoRoundInfo.round
            )
        }
    }
}
