package lotto.service

import config.ImplementationTest
import fixture.LottoBillFixture
import fixture.LottoRoundInfoFixture
import fixture.PurchaseFixture
import io.kotest.matchers.shouldBe
import lotto.domain.repository.LottoBillRepository
import lotto.domain.repository.LottoRoundInfoRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import purchase.domain.repository.PurchaseRepository
import java.time.LocalDateTime
import kotlin.test.Test

@ImplementationTest
class LottoStatisticsServiceTest {
    @Autowired
    private lateinit var lottoRoundInfoRepository: LottoRoundInfoRepository

    @Autowired
    private lateinit var lottoBillRepository: LottoBillRepository

    @Autowired
    private lateinit var purchaseRepository: PurchaseRepository

    @Autowired
    private lateinit var lottoStatisticsService: LottoStatisticsService

    @BeforeEach
    fun setUp() {
        val lottoRoundInfo = lottoRoundInfoRepository.save(
            LottoRoundInfoFixture.매우_긴_진행중인_회차_생성()
        )
        val purchase1 = purchaseRepository.save(
            PurchaseFixture.토스_제공자와_카드로_성공한_결제(3000)
        )

        val purchase2 = purchaseRepository.save(
            PurchaseFixture.토스_제공자와_카드로_성공한_결제(5000)
        )
        lottoBillRepository.save(
            LottoBillFixture.무작위_멤버_영수증_저장(
                lottoRoundInfoId = lottoRoundInfo.id!!,
                purchaseId = purchase1.getId().toString()
            )
        )
        lottoBillRepository.save(
            LottoBillFixture.무작위_멤버_영수증_저장(
                lottoRoundInfoId = lottoRoundInfo.id!!,
                purchaseId = purchase2.getId().toString()
            )
        )
    }

    @Test
    fun `통계 데이터를 업데이트 한다`() {
        val time = LocalDateTime.now()
        val lottoRoundInfo = lottoRoundInfoRepository.findTopByIssueDateLessThanEqualAndDrawDateGreaterThanEqual(
            time
        )!!
        val data = lottoStatisticsService.updateStaticInfoWithCurrentLottoRoundInfo(lottoRoundInfo, time)
        data.updatedAt shouldBe time
        data.memberCount shouldBe 2
        data.totalPurchaseMoney shouldBe 8000
    }
}
