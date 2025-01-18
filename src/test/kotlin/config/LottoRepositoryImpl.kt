package config

import lotto.domain.converter.LottoNumbersConverter
import lotto.domain.entity.Lotto
import lotto.domain.repository.LottoRepository
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.atomic.AtomicLong

open class LottoRepositoryImpl : FakeCrudRepository<Lotto, Long>(), LottoRepository {
    private val lottoNumbersConverter: LottoNumbersConverter = LottoNumbersConverter()
    private val numberIncrementer: AtomicLong = AtomicLong(1)

    override fun extractId(entity: Lotto): Long? {
        return numberIncrementer.getAndIncrement()
    }

    @Transactional
    override fun findLottoByNumbersIn(numbers: List<String>): List<Lotto> {
        return numbers.map {
            save(Lotto(lottoNumbersConverter.convertToEntityAttribute(it)))
        }
    }

    @Transactional
    override fun findLottoByNumber(number: String): Lotto? {
        return save(Lotto(lottoNumbersConverter.convertToEntityAttribute(number)))
    }
}
