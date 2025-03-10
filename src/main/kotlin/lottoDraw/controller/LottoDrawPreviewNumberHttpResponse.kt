package lottoDraw.controller

import lottoDraw.service.dto.LottoNumberCountData
import lottoDraw.service.dto.LottoResultData

data class LottoDrawPreviewNumberHttpResponse(
    val lottoNumberCountResponse: List<LottoNumberCountHttpResponse>,
    val lottoResultResponse: List<LottoResultHttpResponse>
) {

}

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

data class LottoResultHttpResponse(
    val round: Int,
    val matchCount: Int,
    val bonusMatch: Boolean
) {
    companion object {
        @JvmStatic
        fun from(data: List<LottoResultData>): List<LottoResultHttpResponse> {
            return data.map {
                LottoResultHttpResponse(
                    round = it.round,
                    matchCount = it.matchCount,
                    bonusMatch = it.bonusMatch
                )
            }
        }
    }
}
