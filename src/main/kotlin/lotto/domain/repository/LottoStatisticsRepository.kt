package lotto.domain.repository

import lotto.domain.entity.LottoStatistics
import org.springframework.data.jpa.repository.JpaRepository

interface LottoStatisticsRepository : JpaRepository<LottoStatistics, Long> {
    fun findByLottoRoundInfoId(lottoRoundInfoId: Long?): LottoStatistics?
}
