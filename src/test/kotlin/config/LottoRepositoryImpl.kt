package config

import lotto.domain.converter.LottoNumbersConverter
import lotto.domain.entity.Lotto
import lotto.domain.repository.LottoRepository

class LottoRepositoryImpl : FakeCrudRepository<Lotto, Long>(), LottoRepository {

    private val lottoNumbersConverter: LottoNumbersConverter = LottoNumbersConverter()

    override fun extractId(entity: Lotto): Long? {
        return entity.getId()
    }

    override fun findLottoByNumbersIn(numbers: List<String>): List<Lotto> {
        return numbers.map {
            Lotto(lottoNumbersConverter.convertToEntityAttribute(it))
        }
    }

    override fun findLottoByNumber(number: String): Lotto? {
        return Lotto(lottoNumbersConverter.convertToEntityAttribute(number))
    }
}
