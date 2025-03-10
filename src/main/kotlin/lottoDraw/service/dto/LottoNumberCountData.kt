package lottoDraw.service.dto

import lottoDraw.domain.entity.LottoNumberCount

data class LottoNumberCountData(
    val number: Byte,
    val count: Int
) {
    companion object {
        @JvmStatic
        fun from(lottoNumberCount: LottoNumberCount): LottoNumberCountData {
            return LottoNumberCountData(
                number = lottoNumberCount.number,
                count = lottoNumberCount.count
            )
        }
    }
}
