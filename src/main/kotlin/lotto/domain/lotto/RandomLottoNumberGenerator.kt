package lotto.domain.lotto

import common.business.Implementation
import kotlin.random.Random

@Implementation
class RandomLottoNumberGenerator : LottoNumberGenerator {
    override fun generate(): IssuedLotto {
        return IssuedLotto(
            IssueStatus.AUTO, Lotto(
                generateSequence { Random.nextInt(1, 46) }.distinct()
                    .take(6)
                    .toList()
            )
        )
    }

    override fun generate(number: Int): List<IssuedLotto> {
        val generatedLottoes = mutableSetOf<IssuedLotto>()

        while (generatedLottoes.size < number) {
            generatedLottoes.add(generate())
        }
        return generatedLottoes.toList()
    }
}
