package lotto.domain.repository

import lotto.domain.Lotto
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface LottoRepository : CrudRepository<Lotto, Long> {

    @Query("SELECT * FROM Lotto l WHERE l.numbers IN :numbers", nativeQuery = true)
    fun findLottoByNumbersIn(@Param("numbers") numbers: List<String>): List<Lotto>

    @Query("SELECT * FROM Lotto l WHERE l.numbers = :number",nativeQuery = true)
    fun findLottoByNumber(@Param("number") number: String): Lotto?
}
