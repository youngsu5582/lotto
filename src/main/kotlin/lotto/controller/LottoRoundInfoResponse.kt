package lotto.controller

import lotto.domain.entity.LottoStatus
import lotto.service.dto.LottoRoundInfoData
import java.time.LocalDateTime

data class LottoRoundInfoResponse(
    val round: Long,
    val endDate: LocalDateTime,
    val drawDate: LocalDateTime,
    val status: LottoStatus
) {
    companion object {
        fun from(lottoRoundInfoData: LottoRoundInfoData) = with(lottoRoundInfoData) {
            LottoRoundInfoResponse(
                round = round,
                endDate = endDate,
                drawDate = drawDate,
                status = status
            )
        }
    }
}
