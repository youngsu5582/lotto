package lottoDraw.domain.repository

import lottoDraw.domain.entity.LottoDrawEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface LottoDrawRepository : CrudRepository<LottoDrawEntity, Int> {
    @Query(
        """
        SELECT l
        FROM LottoDrawEntity l
        WHERE
            (
                (CASE WHEN l.first  IN :lottoNumbers THEN 1 ELSE 0 END) +
                (CASE WHEN l.second IN :lottoNumbers THEN 1 ELSE 0 END) +
                (CASE WHEN l.third  IN :lottoNumbers THEN 1 ELSE 0 END) +
                (CASE WHEN l.fourth IN :lottoNumbers THEN 1 ELSE 0 END) +
                (CASE WHEN l.fifth  IN :lottoNumbers THEN 1 ELSE 0 END) +
                (CASE WHEN l.sixth  IN :lottoNumbers THEN 1 ELSE 0 END) +
                (CASE WHEN l.bonus IN :lottoNumbers THEN 1 ELSE 0 END)
            ) >= 5
    """
    )
    fun findAllWithFiveOrMoreMatches(
        @Param("lottoNumbers") lottoNumbers: List<Byte>
    ): List<LottoDrawEntity>
}
