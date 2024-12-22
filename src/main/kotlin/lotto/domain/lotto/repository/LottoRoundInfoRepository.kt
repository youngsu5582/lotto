package lotto.domain.lotto.repository

import lotto.domain.lotto.LottoRoundInfo
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.time.LocalDateTime

interface LottoRoundInfoRepository : CrudRepository<LottoRoundInfo, Long> {
    @Query(
        "SELECT l FROM LottoRoundInfo l WHERE :dateTime BETWEEN l.startDate AND l.drawDate ORDER BY l.startDate DESC LIMIT 1"
    )
    fun findTopByIssueDateLessThanEqualAndDrawDateGreaterThanEqual(
        dateTime: LocalDateTime
    ): LottoRoundInfo?
}
