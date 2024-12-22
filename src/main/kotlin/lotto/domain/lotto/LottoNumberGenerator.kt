package lotto.domain.lotto

interface LottoNumberGenerator {
    fun generate(): IssuedLotto
    fun generate(number: Int): List<IssuedLotto>
}
