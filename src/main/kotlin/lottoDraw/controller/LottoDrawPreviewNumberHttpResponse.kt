package lottoDraw.controller

import lottoDraw.domain.vo.LottoDrawResultData
import lottoDraw.service.dto.LottoNumberCountData
import java.time.LocalDate

data class LottoDrawPreviewNumberHttpResponse(
    val lottoNumberCountResponse: List<LottoNumberCountHttpResponse>,
    val lottoResultResponse: List<LottoDrawResultHttpResponse>
)

data class LottoNumberCountHttpResponse(
    val number: Byte,
    val count: Int
) {
    companion object {
        @JvmStatic
        fun from(data: List<LottoNumberCountData>): List<LottoNumberCountHttpResponse> {
            return data.map {
                LottoNumberCountHttpResponse(
                    number = it.number,
                    count = it.count
                )
            }
        }
    }
}

data class LottoDrawResultHttpResponse(
    val round: Int,
    val lottoDrawResponse: LottoDrawHttpResponse,
    val drawResultResponse: DrawResultHttpResponse
) {
    companion object {
        @JvmStatic
        fun from(data: List<LottoDrawResultData>): List<LottoDrawResultHttpResponse> {
            return data.map {
                with(it) {
                    LottoDrawResultHttpResponse(
                        round = round,
                        lottoDrawResponse = LottoDrawHttpResponse(
                            date = date,
                            numbers = numbers.toBytes(),
                            bonus = bonus.toByte()
                        ),
                        drawResultResponse = DrawResultHttpResponse(
                            matchCount = matchCount,
                            bonusMatch = bonusMatch
                        ),
                    )
                }
            }
        }
    }
}

data class LottoDrawHttpResponse(
    val date: LocalDate,
    val numbers: List<Byte>,
    val bonus: Byte,
)

data class DrawResultHttpResponse(
    val matchCount: Int,
    val bonusMatch: Boolean
)
