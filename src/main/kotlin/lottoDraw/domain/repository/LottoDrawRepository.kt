package lottoDraw.domain.repository

import lottoDraw.domain.entity.LottoDraw
import lottoDraw.domain.vo.LottoResult
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface LottoDrawRepository : CrudRepository<LottoDraw, Int> {
    @Query("""
        SELECT new lottoDraw.domain.vo.LottoResult(
            l.round,
            (
                (CASE WHEN l.first  IN :lottoNumbers THEN 1 ELSE 0 END) +
                (CASE WHEN l.second IN :lottoNumbers THEN 1 ELSE 0 END) +
                (CASE WHEN l.third  IN :lottoNumbers THEN 1 ELSE 0 END) +
                (CASE WHEN l.fourth IN :lottoNumbers THEN 1 ELSE 0 END) +
                (CASE WHEN l.fifth  IN :lottoNumbers THEN 1 ELSE 0 END) +
                (CASE WHEN l.sixth  IN :lottoNumbers THEN 1 ELSE 0 END)
            ),
            CASE WHEN l.bonus IN :lottoNumbers THEN true ELSE false END
        )
        FROM LottoDraw l
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
    """)
    fun findAllWithFiveOrMoreMatches(
        @Param("lottoNumbers") lottoNumbers: List<Byte>
    ): List<LottoResult>
}
