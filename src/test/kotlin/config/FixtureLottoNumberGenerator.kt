package config

import lotto.domain.implementation.LottoNumberGenerator
import lotto.domain.vo.LottoNumbers

class FixtureLottoNumberGenerator(
    private val numbers: List<List<Int>>,
) : LottoNumberGenerator {
    private var count = 0

    private fun generate(): List<Int> {
        return numbers[count++]
    }

    override fun generate(number: Int): LottoNumbers {
        require(number <= numbers.size - count) {
            "생성 가능한 로또 개수를 초과했습니다."
        }

        return LottoNumbers(List(number) { generate() })
    }
}
