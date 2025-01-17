package lotto.service

import app.TestConfig
import config.ImplementationTest
import lotto.domain.entity.LottoRoundInfo
import lotto.domain.repository.LottoRoundInfoRepository
import lotto.domain.vo.Currency
import lotto.domain.vo.LottoNumbers
import lotto.domain.vo.LottoPurchaseRequest
import lotto.domain.vo.PurchaseType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import java.math.BigDecimal
import java.time.LocalDateTime

@ImplementationTest
@Import(TestConfig::class)
class LottoPurchaseServiceTest {

    @Autowired
    private lateinit var lottoPurchaseService: LottoPurchaseService

    @Autowired
    private lateinit var lottoRoundInfoRepository: LottoRoundInfoRepository

    @BeforeEach
    fun setUp() {
        val dateTime = LocalDateTime.now()
        lottoRoundInfoRepository.save(
            LottoRoundInfo(
                null,
                round = 1,
                startDate = dateTime.minusHours(1),
                endDate = dateTime.plusHours(1),
                drawDate = dateTime.plusHours(2),
                paymentDeadline = dateTime.plusYears(1),
            ),
        )
    }

    @Test
    fun `d`() {
        val bill = lottoPurchaseService.purchase(
            lottoPurchaseRequest = LottoPurchaseRequest(
                purchaseType = PurchaseType.CARD,
                currency = Currency.KRW,
                amount = BigDecimal(1000),
                paymentKey = "paymentKey",
                orderId = "orderId"
            ),
            lottoNumbers = LottoNumbers(listOf(listOf(1, 14, 17, 19, 21, 34)))
        )

    }
}
