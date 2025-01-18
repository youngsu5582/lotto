package config

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import lotto.domain.converter.LottoNumbersConverter
import lotto.domain.entity.Lotto
import lotto.domain.repository.LottoRepository
import org.springframework.boot.test.context.TestComponent
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.atomic.AtomicLong

@TestComponent
class LottoRepositoryImpl : FakeCrudRepository<Lotto, Long>(), LottoRepository {
    private val lottoNumbersConverter: LottoNumbersConverter = LottoNumbersConverter()
    private val numberIncrementer: AtomicLong = AtomicLong(1)

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun extractId(entity: Lotto): Long? {
        return numberIncrementer.getAndIncrement()
    }

    @Transactional
    override fun <S : Lotto> save(entity: S): S {
        entityManager.persist(entity)
        return super.save(entity)
    }

    @Transactional
    override fun deleteAll() {
        entityManager.clear()
        super.deleteAll()
    }

    @Transactional
    override fun deleteById(id: Long) {
        entityManager.remove(id)
        super.deleteById(id)
    }

    @Transactional
    override fun delete(entity: Lotto) {
        entityManager.remove(entity)
        super.delete(entity)
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
