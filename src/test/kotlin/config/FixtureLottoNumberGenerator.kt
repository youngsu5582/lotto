package config

import lotto.domain.lotto.IssueStatus
import lotto.domain.lotto.IssuedLotto
import lotto.domain.lotto.Lotto
import lotto.domain.lotto.LottoNumberGenerator

class FixtureLottoNumberGenerator(
    private val numbers: List<List<Int>>
) : LottoNumberGenerator {
    private var count = 0

    override fun generate(): IssuedLotto {
        return IssuedLotto(IssueStatus.AUTO, Lotto(numbers[count++]))
    }

    override fun generate(number: Int): List<IssuedLotto> {
        require(number <= numbers.size - count) {
            "생성 가능한 로또 개수를 초과했습니다."
        }

        return List(number) { generate() }
    }
}

