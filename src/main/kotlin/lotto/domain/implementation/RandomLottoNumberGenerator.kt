package lotto.domain.implementation

import common.business.Implementation
import lotto.domain.vo.LottoNumbersPack
import kotlin.random.Random

@Implementation
class RandomLottoNumberGenerator : LottoNumberGenerator {
    override fun generate(number: Int): LottoNumbersPack {
        val generatedLottoes = mutableSetOf<List<Int>>()

        while (generatedLottoes.size < number) {
            generatedLottoes.add(generate())
        }
        return LottoNumbersPack(generatedLottoes.toList())
    }

    private fun generate(): List<Int> {
        return generateSequence { Random.nextInt(1, 46) }.distinct()
            .take(6)
            .toList()
    }
}
