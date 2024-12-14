package lotto.domain.repository

import lotto.domain.LottoRoundInfo
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.time.LocalDateTime

interface LottoRoundInfoRepository : CrudRepository<LottoRoundInfo, Long> {
    @Query(
        "SELECT l FROM LottoRoundInfo l WHERE :dateTime BETWEEN l.startDate AND l.endDate ORDER BY l.startDate DESC LIMIT 1"
    )
    fun findTopByIssueDateLessThanEqualAndEndDateGreaterThanEqual(
        dateTime: LocalDateTime
    ): LottoRoundInfo?
}
