package lotto.domain.implementation

import lotto.domain.vo.LottoNumbers

interface LottoNumberGenerator {
    fun generate(number: Int): LottoNumbers
}
