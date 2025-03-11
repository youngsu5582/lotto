package lotto.domain.implementation

import lotto.domain.vo.LottoNumbersPack

interface LottoNumberGenerator {
    fun generate(number: Int): LottoNumbersPack
}
