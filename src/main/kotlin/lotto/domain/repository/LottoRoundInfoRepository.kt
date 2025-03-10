package lotto.domain.repository

import lotto.domain.entity.LottoRoundInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface LottoRoundInfoRepository : JpaRepository<LottoRoundInfo, Long> {
    @Query(
        "SELECT l FROM LottoRoundInfo l WHERE :dateTime BETWEEN l.startDate AND l.drawDate ORDER BY l.startDate DESC LIMIT 1",
    )
    fun findTopByIssueDateLessThanEqualAndDrawDateGreaterThanEqual(dateTime: LocalDateTime): LottoRoundInfo?
}
