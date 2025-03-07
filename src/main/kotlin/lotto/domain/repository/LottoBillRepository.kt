package lotto.domain.repository

import lotto.domain.entity.LottoBill
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface LottoBillRepository : JpaRepository<LottoBill, Long> {
    fun findByMemberId(memberId: String): List<LottoBill>

    @Query("SELECT lb.purchaseId from LottoBill lb where lb.lottoRoundInfoId=:lottoRoundInfoId")
    fun findPurchaseIdsByLottoRoundInfo(lottoRoundInfoId: Long): List<String>

    fun findAllByLottoRoundInfoId(lottoRoundInfoId: Long): List<LottoBill>
}
