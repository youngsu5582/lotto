package lotto.domain.implementation

import common.business.Implementation
import lotto.domain.vo.LottoNumbers
import kotlin.random.Random

@Implementation
class RandomLottoNumberGenerator : LottoNumberGenerator {
    override fun generate(number: Int): LottoNumbers {
        val generatedLottoes = mutableSetOf<List<Int>>()

        while (generatedLottoes.size < number) {
            generatedLottoes.add(generate())
        }
        return LottoNumbers(generatedLottoes.toList())
    }

    private fun generate(): List<Int> {
        return generateSequence { Random.nextInt(1, 46) }.distinct()
            .take(6)
            .toList()
    }
}
