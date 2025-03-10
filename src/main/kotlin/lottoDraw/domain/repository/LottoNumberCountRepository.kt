package lottoDraw.domain.repository

import lottoDraw.domain.entity.LottoNumberCount
import org.springframework.data.repository.CrudRepository

interface LottoNumberCountRepository : CrudRepository<LottoNumberCount, Byte> {
    fun findByNumber(number: Byte): LottoNumberCount
    fun findByNumberIn(numbers: List<Byte>): Iterable<LottoNumberCount>
}
