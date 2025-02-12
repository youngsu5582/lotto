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
        fun from(lottoPublish: LottoRoundInfo): LottoRoundInfoData {
            return LottoRoundInfoData(
                startDate = lottoPublish.startDate,
                endDate = lottoPublish.endDate,
                drawDate = lottoPublish.drawDate,
                status = lottoPublish.status,
                paymentDeadline = lottoPublish.paymentDeadline,
                round = lottoPublish.round
            )
        }
    }
}
